package com.fitness.aiservice.models;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class Activity {
    private String id;
    private String type; // e.g., Running, Cycling, Swimming
    private String userId;
    private Integer duration; // in minutes
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, String> additionalMetrics; // Additional metadata like location, device used, etc.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
