package com.example.kyfbackend.repos;

import com.example.kyfbackend.models.FoodIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FoodIngredientRepository extends MongoRepository<FoodIngredient, String> {
    FoodIngredient findByName(String name);
}
