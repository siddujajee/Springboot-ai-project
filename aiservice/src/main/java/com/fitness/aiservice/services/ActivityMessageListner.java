package com.fitness.aiservice.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.models.Activity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ActivityMessageListner {

  @RabbitListener(queues = "${rabbitmq.queue.name}")
  public void processActivity(Activity activity) {
      log.info("Processing activity: {}", activity.getId());
  }
}
