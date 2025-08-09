package com.fitness.activityservice.models;

import org.springframework.data.mongodb.core.mapping.Document;

public enum ActivityType {
    RUNNING,
    CYCLING,
    SWIMMING,
    WALKING,
    YOGA,
    WEIGHTLIFTING,
    OTHER
}
