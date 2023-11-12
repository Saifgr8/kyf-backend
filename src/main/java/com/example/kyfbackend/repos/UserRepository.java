package com.example.kyfbackend.repos;

import com.example.kyfbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String name);
    User findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
    User findByResetPasswordToken(String token);
}
