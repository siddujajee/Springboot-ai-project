package com.fitness.activityservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.services.ActivityService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

  private ActivityService activityService;

  @PostMapping
  public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest, @RequestHeader("X-User-Id") String userId) {
    if(userId != null){
      activityRequest.setUserId(userId);
    }
    return ResponseEntity.ok(activityService.trackActivity(activityRequest));
  }

  @GetMapping
  public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-Id") String userId) {
    return ResponseEntity.ok(activityService.getUserActivities(userId));
  }

  @GetMapping("/{activityId}")
  public ResponseEntity<ActivityResponse> getActivity(@PathVariable String activityId) {
    ActivityResponse activityResponse = activityService.getActivityById(activityId);
    return ResponseEntity.ok(activityResponse);
  }

}
