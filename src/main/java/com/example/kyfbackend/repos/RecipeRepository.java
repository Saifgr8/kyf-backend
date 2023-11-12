package com.example.kyfbackend.repos;

import com.example.kyfbackend.models.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByOwner(String owner);
    Recipe findByName(String name);
}
