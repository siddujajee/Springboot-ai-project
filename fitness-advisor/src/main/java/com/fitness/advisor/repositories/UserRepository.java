package com.fitness.advisor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitness.advisor.models.User;

public interface UserRepository extends JpaRepository<User, String> {
    // This interface will automatically provide CRUD operations for User entity
    // Additional custom query methods can be defined here if needed
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
