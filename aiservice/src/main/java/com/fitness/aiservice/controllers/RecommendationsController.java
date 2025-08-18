package com.fitness.aiservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.aiservice.models.Recommendation;
import com.fitness.aiservice.services.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationsController {
  private final RecommendationService recommendationService;

  @GetMapping("/users/{userId}")
  public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId) {
    return ResponseEntity.ok(recommendationService.getUserRecommendations(userId));
  }

  @GetMapping("/activity/{activityId}")
  public ResponseEntity<Recommendation> getActivityRecommendations(@PathVariable String activityId) {
    return ResponseEntity.ok(recommendationService.getActivityRecommendations(activityId));
  }
}
