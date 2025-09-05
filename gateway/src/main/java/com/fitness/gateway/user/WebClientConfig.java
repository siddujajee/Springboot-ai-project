package com.fitness.gateway.user;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class WebClientConfig {
  @Bean
  @LoadBalanced // used to resolve the service instances using service name used to register
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder.baseUrl("http://USER-SERVICE").build();
  }
}