package com.fitness.aiservice.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

// this service interacts with the Gemini API and retrieves data
@Service
public class GeminiService {
  private final WebClient webClient;

  // get gemini api data
  @Value("${GEMINI_API_URL}")
  private String geminiUrl;

  @Value("${GEMINI_API_KEY}")
  private String geminiApiKey;

  public GeminiService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  // following this structure below to build requestBody (can refer the ai model's documentation for this structure)
  // this is how the request should be sent
  // "contents": [
  //   {
  //     "parts": [
  //       {
  //         "text": "will ai colonise in future and take over human world?"
  //       }
  //     ]
  //   }
  // ]

  // will have to create an instance of this service and call below method to get response from gemini
  public String getGeminiData(String feededData) {
    Map<String, Object> requestBody = Map.of("contents", new Object[]{
      Map.of("parts", new Object[]{
        Map.of("text", feededData)
      })
    });

    String response = webClient.post()
      .uri(geminiUrl + geminiApiKey)
      .header("Content-Type", "application/json")
      .bodyValue(requestBody)
      .retrieve()
      .bodyToMono(String.class)
      .block();

    return response;
  }
}
