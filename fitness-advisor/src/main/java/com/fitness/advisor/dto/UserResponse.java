package com.fitness.advisor.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String firstname;
    private String lastname;
    private LocalDateTime dateOfBirth;
    private String email;
    private int age;
    private double weight;
    private double height;
}
