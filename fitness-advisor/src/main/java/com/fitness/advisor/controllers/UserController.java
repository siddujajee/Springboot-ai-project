package com.fitness.advisor.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.advisor.Services.UserService;
import com.fitness.advisor.dto.RegisterRequest;
import com.fitness.advisor.dto.UserResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

		private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
				// Logic to fetch user profile by userId
				// For now, returning a placeholder response
				return ResponseEntity.ok(userService.getUserProfile(userId));
		}

		@PostMapping("/register")
		public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request){
				// Logic to register a new user
				// For now, returning a placeholder response
				return ResponseEntity.ok(userService.registerUser(request));
		}

		
}
