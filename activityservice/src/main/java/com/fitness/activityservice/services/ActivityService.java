package com.fitness.activityservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.models.Activity;
import com.fitness.activityservice.repositories.ActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // it will generate a constructor with required arguments only such as final fields and non-null fields, this is a primary difference from @AllArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
      Activity activity = Activity.builder()
        .userId(activityRequest.getUserId())
        .type(activityRequest.getType())
        .duration(activityRequest.getDuration())
        .caloriesBurned(activityRequest.getCaloriesBurned())
        .startTime(activityRequest.getStartTime())
        .additionalMetrics(activityRequest.getAdditionalMetrics())
        .build();
        
        // Save the activity to the database
        Activity savedActivity = activityRepository.save(activity);
        // Convert the saved activity to a response DTO
        return convertToResponse(savedActivity);
    }

    public List<ActivityResponse> getUserActivities(String userId) {
      List<Activity> activities = activityRepository.findByUserId(userId);
      return activities.stream()
        .map(this::convertToResponse)
        .collect((Collectors.toList()));
    }

    public ActivityResponse getActivityById(String activityId) {
      Activity activity = activityRepository.findById(activityId)
        .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
      return convertToResponse(activity);
    }

     private ActivityResponse convertToResponse(Activity activity) {
      ActivityResponse response = new ActivityResponse();
      response.setId(activity.getId());
      response.setUserId(activity.getUserId());
      response.setType(activity.getType());
      response.setDuration(activity.getDuration());
      response.setCaloriesBurned(activity.getCaloriesBurned());
      response.setStartTime(activity.getStartTime());
      response.setAdditionalMetrics(activity.getAdditionalMetrics());
      response.setCreatedAt(activity.getCreatedAt());
      response.setUpdatedAt(activity.getUpdatedAt());
      return response;
    }

}
