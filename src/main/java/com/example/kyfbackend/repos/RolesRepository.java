package com.example.kyfbackend.repos;

import com.example.kyfbackend.security.ERole;
import com.example.kyfbackend.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RolesRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
