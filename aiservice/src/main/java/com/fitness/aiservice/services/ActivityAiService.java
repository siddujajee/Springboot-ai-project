package com.fitness.aiservice.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.models.Activity;
import com.fitness.aiservice.models.Recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// this service will actually work with gemini and generate the recommendation
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
  private final GeminiService geminiService;
  public Recommendation generateRecommendation(Activity activity) {
    String prompt = createPromptForActivity(activity);
    String geminiResponse = geminiService.getGeminiData(prompt);
    // log.info("Gemini API response: {}", geminiResponse);
    return processAiResponse(activity, geminiResponse);
  }

  private Recommendation processAiResponse(Activity activity, String aiResponse) {
    try{
      ObjectMapper mapper = new ObjectMapper();
      // below line will parse the AI response JSON
      JsonNode rootNode = mapper.readTree(aiResponse);
      // below line will extract the "candidates" field
      JsonNode textNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");
      
      // replace all the unwanted placeholders from the response
      String jsonContent = textNode.asText().replaceAll("```json\\n", "").replaceAll("\\n```", "").trim();
      // log.info("Processed AI response for activity: {}", jsonContent);

      // creating summary section JSON
      JsonNode summaryJsonNode = mapper.readTree(jsonContent);
      StringBuilder fullSummary = new StringBuilder();

      // format the summary section
      writeSummaryOfExercise(summaryJsonNode, fullSummary);

      // extract area of improvements
      List<String> areaOfImprovements = extractAreaOfImprovements(summaryJsonNode);

      // extract suggestions
      List<String> suggestions = extractSuggestions(summaryJsonNode);

      // extract safety
      List<String> safetyMetrics = extractSafetyMetrics(summaryJsonNode);

      return Recommendation.builder().activityId(activity.getId()).userId(activity.getUserId())
        .activityType(activity.getType()).activitySummary(fullSummary.toString().trim())
        .improvements(areaOfImprovements).suggestions(suggestions).safety(safetyMetrics).createdAt(LocalDateTime.now()).build();

    } catch (Exception e) {
      log.error("Error processing AI response for activity: {}", e.getMessage());
      return createDefaultRecommendation(activity); // just so we have a return value
    }
  }

  private Recommendation createDefaultRecommendation(Activity activity) {
    return Recommendation.builder().activityId(activity.getId()).userId(activity.getUserId())
      .activityType(activity.getType()).activitySummary("Unable to generate a summary, try concerning fitness professional")
      .improvements(Collections.singletonList("Unable to generate improvements"))
      .suggestions(Collections.singletonList("Unable to generate suggestions"))
      .safety(Collections.singletonList("Unable to generate safety metrics"))
      .createdAt(LocalDateTime.now()).build();
  }

  private List<String> extractSafetyMetrics(JsonNode summaryJsonNode) {
    List<String> safetyMetrics = new ArrayList<>();
    if (!summaryJsonNode.path("safety").isMissingNode() && summaryJsonNode.path("safety").isArray()) {
      summaryJsonNode.path("safety").forEach(metricNode -> {
        String safetyGuidline = metricNode.path("guidline").asText();
        String safetyDescription = metricNode.path("description").asText();
        safetyMetrics.add("Metric: " + safetyGuidline + ", Value: " + safetyDescription);
      });
    }
    return safetyMetrics.isEmpty() ? List.of("No safety metrics identified.") : safetyMetrics;
  }

  private List<String> extractSuggestions(JsonNode summaryJsonNode) {
    List<String> suggestions = new ArrayList<>();
    if (!summaryJsonNode.path("suggestions").isMissingNode() && summaryJsonNode.path("suggestions").isArray()) {
      summaryJsonNode.path("suggestions").forEach(suggestionNode -> {
        String workout = suggestionNode.path("workout").asText();
        String description = suggestionNode.path("description").asText();
        suggestions.add("Workout: " + workout + ", Description: " + description);
      });
    }
    return suggestions.isEmpty() ? List.of("No specific suggestions identified.") : suggestions;
  }

  private List<String> extractAreaOfImprovements(JsonNode summaryJsonNode) {
    List<String> areaOfImprovements = new ArrayList<>();
    if(!summaryJsonNode.path("Area of improvements").isMissingNode() && summaryJsonNode.path("Area of improvements").isArray()){
      summaryJsonNode.path("Area of improvements").forEach(improvementNode -> {
        String area = improvementNode.path("area").asText();
        String recommendation = improvementNode.path("recommendation").asText();
        areaOfImprovements.add("Area: " + area + ", Recommendation: " + recommendation);
      });
    }
    return areaOfImprovements.isEmpty() ? List.of("No specific areas of improvement identified.") : areaOfImprovements;
  }

  private void writeSummaryOfExercise(JsonNode summaryJsonNode, StringBuilder fullSummary) {
    fullSummary.append("Exercise Summary:\n");
    if(!summaryJsonNode.path("analysis").isMissingNode()){
      fullSummary.append(" - Analysis: ").append(summaryJsonNode.path("analysis").asText()).append("\n");
    }
    if(!summaryJsonNode.path("pace").isMissingNode()){
      fullSummary.append(" - Pace: ").append(summaryJsonNode.path("pace").asText()).append("\n");
    }
    if(!summaryJsonNode.path("heartRate").isMissingNode()){
      fullSummary.append(" - Heart Rate: ").append(summaryJsonNode.path("heartRate").asText()).append("\n");
    }
    if(!summaryJsonNode.path("caloriesBurned").isMissingNode()){
      fullSummary.append(" - Calories Burned: ").append(summaryJsonNode.path("caloriesBurned").asText()).append("\n");
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
        "caloriesBurned": "Provide the calories burned information.",

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
      }

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
