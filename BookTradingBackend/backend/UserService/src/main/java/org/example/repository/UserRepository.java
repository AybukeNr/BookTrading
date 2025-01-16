package org.example.repository;

import org.example.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);         // Null güvenliği için Optional
    Optional<User> findByUsername(String username);   // Null güvenliği için Optional
    Optional<User> findByPhoneNumber(String phone);   // Null güvenliği için Optional
}
