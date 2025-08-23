package com.fitness.aiservice.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.models.Activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// this service will actually work with gemini and generate the recommendation
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
  private final GeminiService geminiService;
  public String generateRecommendation(Activity activity) {
    String prompt = createPromptForActivity(activity);
    String geminiResponse = geminiService.getGeminiData(prompt);
    log.info("Gemini API response: {}", geminiResponse);
    processAiResponse(activity, geminiResponse);
    return geminiResponse;
  }

  private void processAiResponse(Activity activity, String aiResponse) {
    try{
      ObjectMapper mapper = new ObjectMapper();
      // below line will parse the AI response JSON
      JsonNode rootNode = mapper.readTree(aiResponse);
      // below line will extract the "candidates" field
      JsonNode textNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");
      
      // replace all the unwanted placeholders from the response
      String jsonContent = textNode.asText().replaceAll("```\\n", "").replaceAll("\\n", "").trim();
      log.info("Processed AI response for activity {}: {}", activity.getId(), jsonContent);
    } catch (Exception e) {
      log.error("Error processing AI response for activity {}: {}", activity.getId(), e.getMessage());
    }
  }

  public String createPromptForActivity(Activity activity) {
    // request for JSON formate request
    // Reasons: 1. easier to parse
    //          2. readability
    return String.format("""
      Based on the following activity data, provide personalized fitness recommendations in exact JSON Formate:
      {
        "analysis": "Provide a brief analysis of the activity.",
        "pace": "Provide the pace information.",
        "heartRate": "Provide the heart rate information.",
        "caloriesBurned": "Provide the calories burned information."
      },
      "Area of improvements": [
        {
          "area": "Provide the area of improvement.",
          "recommendation": "Provide a recommendation for improvement."
        }
      ],
      "suggestions": [
      {
        "workout": "Provide a suggestion for improvement.",
        "description": "Detailed workout description."
      }],
      "safety": [
        {
          "guideline": "Provide a safety guideline.",
          "description": "Detailed safety description."
        } 
      ]
        
      analyse this activity
      {
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Matrix: %s
      }

      ensure that the response is in valid JSON format mentioned above strictly.

      """,
      activity.getType(),
      activity.getDuration(),
      activity.getCaloriesBurned(),
      activity.getAdditionalMetrics()
    );
  }

}
