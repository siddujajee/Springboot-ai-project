package com.fitness.activityservice.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fitness.activityservice.models.ActivityType;

import lombok.Data;

@Data
public class ActivityResponse {
    
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration; // in minutes
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, String> additionalMetrics; // Additional metadata like location, device used, etc.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
