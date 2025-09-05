package com.fitness.gateway.user;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final WebClient userServiceWebClient;

  public Mono<Boolean> isValidUserId(String userId) {
    return userServiceWebClient.get()
      .uri("/api/users/{userId}/validate", userId)
      .retrieve()
      .bodyToMono(Boolean.class)
      .onErrorResume(e -> {
        if (e instanceof WebClientResponseException) {
          WebClientResponseException ex = (WebClientResponseException) e;
          if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("User not found"));
          }
        }
        return Mono.error(e);
      });
  }

  public Mono<UserResponse> registerUser(RegisterRequest request) {
    log.info("Registering user: {}", request.getEmail());
    return userServiceWebClient.post()
      .uri("/api/users/register")
      .bodyValue(request)
      .retrieve()
      .bodyToMono(UserResponse.class)
      .onErrorResume(WebClientResponseException.class, e -> {
        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
          return Mono.error(new RuntimeException("Bad Request : " + e.getMessage()));
        } else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
          return Mono.error(new RuntimeException("Internal Server Error : " + e.getMessage()));
        } else {
          return Mono.error(new RuntimeException("Unexpected Error : " + e.getMessage()));
        }
      });
  }
}
