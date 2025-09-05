package com.fitness.advisor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.advisor.dto.RegisterRequest;
import com.fitness.advisor.dto.UserResponse;
import com.fitness.advisor.models.User;
import com.fitness.advisor.repositories.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public UserResponse getUserProfile(String userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    UserResponse userResponse = new UserResponse();
    userResponse.setUsername(user.getUsername());
    userResponse.setFirstname(user.getFirstname());
    userResponse.setLastname(user.getLastname());
    userResponse.setEmail(user.getEmail());
    userResponse.setWeight(user.getWeight());
    userResponse.setHeight(user.getHeight());
    return userResponse;
    // Logic to fetch user profile by userId    
  }

  public UserResponse registerUser(RegisterRequest request) {
    if(userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
      User existingUser = userRepository.findByEmail(request.getEmail());
      UserResponse userResponse = new UserResponse();
      userResponse.setId(existingUser.getId());
      userResponse.setKeyCloakUserId(existingUser.getKeyCloakUserId());
      userResponse.setUsername(existingUser.getUsername());
      userResponse.setFirstname(existingUser.getFirstname());
      userResponse.setLastname(existingUser.getLastname());
      userResponse.setEmail(existingUser.getEmail());
      userResponse.setWeight(existingUser.getWeight());
      userResponse.setHeight(existingUser.getHeight());
      return userResponse;
    }

    User user = new User(); 
    user.setUsername(request.getUsername());
    user.setFirstname(request.getFirstname());
    user.setKeyCloakUserId(request.getKeyCloakUserId());
    user.setLastname(request.getLastname());
    user.setEmail(request.getEmail());
    user.setWeight(request.getWeight());
    user.setHeight(request.getHeight());
    user.setPassword(request.getPassword());
    userRepository.save(user);
    User savedUser = user;
    UserResponse userResponse = new UserResponse();
    userResponse.setId(savedUser.getId());
    userResponse.setKeyCloakUserId(savedUser.getKeyCloakUserId());
    userResponse.setUsername(savedUser.getUsername());
    userResponse.setFirstname(savedUser.getFirstname());
    userResponse.setLastname(savedUser.getLastname());
    userResponse.setDateOfBirth(savedUser.getDateOfBirth());
    userResponse.setEmail(savedUser.getEmail());
    userResponse.setAge(savedUser.getAge());
    userResponse.setWeight(savedUser.getWeight());
    userResponse.setHeight(savedUser.getHeight());
    return userResponse;
  }

  public Boolean doesUserExist(String userId) {
    if (userRepository.existsById(userId)) {
      return true;
    } else {
      return false;
    }
    // Logic to validate user by userId
    // For now, returning a placeholder response
  }
}