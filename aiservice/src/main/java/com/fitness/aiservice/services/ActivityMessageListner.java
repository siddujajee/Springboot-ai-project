package com.fitness.aiservice.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.models.Activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListner {

  private final ActivityAiService aiservice;

  @RabbitListener(queues = "${rabbitmq.queue.name}")
  public void processActivity(Activity activity) {
    log.info("Processing activity: {}", activity.getId());
    String aiResponse = aiservice.generateRecommendation(activity);
    log.info("AI response: {}", aiResponse);
  }
}
