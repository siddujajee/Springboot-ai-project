package com.fitness.activityservice.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fitness.activityservice.models.Activity;

public interface ActivityRepository extends MongoRepository<Activity, String> {

    List<Activity> findByUserId(String userId);
    // This interface will automatically provide CRUD operations for Activity entity
    // Additional custom query methods can be defined here if needed
    // For example, find activities by userId or activityType can be added later

}
