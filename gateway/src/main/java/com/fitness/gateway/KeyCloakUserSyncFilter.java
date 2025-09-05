package com.fitness.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
// webfilter will intercepts in every request allows us to make inspect, modify or reject the request
public class KeyCloakUserSyncFilter implements WebFilter {
  private final UserService userService;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
    String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
    RegisterRequest registerRequest = getUserDetails(token);
    if(userId == null){
      userId = registerRequest.getKeyCloakUserId();
    }
    if (userId != null && token != null) {
      String finalUserId = userId;
      return userService.isValidUserId(userId)
      .flatMap(exist -> {
        if (exist) {
          log.info("user already exists, skipping sync");
          return Mono.empty();
        } else {
          // register user
          if (registerRequest != null)  {
            return userService.registerUser(registerRequest).then(Mono.empty());
          } else {
            return Mono.empty();
          }
        }
      }).then(Mono.defer(() -> {
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
          .header("X-User-Id", finalUserId)
          .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
      }));
    }
    // If userId or token is missing, just continue the filter chain
    return chain.filter(exchange);
  }

  private RegisterRequest getUserDetails(String token) {
    try{
      String tokenWithoutBearer = token.replace("Bearer ", "").trim();
      SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
      JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
      RegisterRequest registerRequest = new RegisterRequest();
      registerRequest.setEmail(claimsSet.getStringClaim("email"));
      registerRequest.setFirstname(claimsSet.getStringClaim("given_name"));
      registerRequest.setLastname(claimsSet.getStringClaim("family_name"));
      registerRequest.setKeyCloakUserId(claimsSet.getStringClaim("sub"));
      registerRequest.setPassword("User@12345678");
      registerRequest.setUsername(claimsSet.getStringClaim("preferred_username"));
      return registerRequest;
    } catch (Exception e) {
      log.error("Failed to parse token", e);
      return null;
    }
  }
}
