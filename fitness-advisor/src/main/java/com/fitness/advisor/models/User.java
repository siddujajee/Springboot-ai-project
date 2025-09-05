package com.fitness.advisor.models;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String keyCloakUserId;
    
    @Column(unique = true, nullable = false)
    private String username;

    private String firstname;
    private String lastname;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfBirth;

    @CreationTimestamp
    private LocalDateTime registrationDate;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private int age;
    private double weight;
    private double height;
}
