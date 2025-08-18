package com.fitness.aiservice.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.aiservice.models.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecommendationService {
  private final RecommendationRepository recommendationRepository;

  public List<Recommendation> getUserRecommendations(String userId) {
    return recommendationRepository.findByUserId(userId);
  }

  public Recommendation getActivityRecommendations(String activityId) {
    return recommendationRepository.findByActivityId(activityId).orElseThrow(() -> 
        new RuntimeException("No recommendations found for this Activity"));
  }
}
