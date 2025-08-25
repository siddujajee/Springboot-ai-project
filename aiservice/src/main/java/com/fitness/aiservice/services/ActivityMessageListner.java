package com.fitness.aiservice.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.models.Activity;
import com.fitness.aiservice.models.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListner {

  private final ActivityAiService aiservice;
  private final RecommendationRepository recommendationRepository;

  @RabbitListener(queues = "${rabbitmq.queue.name}")
  public void processActivity(Activity activity) {
    // log.info("Processing activity: {}", activity.getId());
    Recommendation aiResponse = aiservice.generateRecommendation(activity);
    recommendationRepository.save(aiResponse);
    // log.info("AI response: {}", aiResponse);
  }
}
