package com.example.kyfbackend.controllers;

import com.example.kyfbackend.models.FoodIngredient;
import com.example.kyfbackend.repos.FoodIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Autowired
    private FoodIngredientRepository foodIngredientRepository;
    // CREATE food ingredient
    @PostMapping
    public FoodIngredient createFoodIngredient(@RequestBody FoodIngredient foodIngredient) {
        return foodIngredientRepository.save(foodIngredient);
    }

    // READ all food ingredients
    @GetMapping
    public List<FoodIngredient> getAllFoodIngredients() {
        return foodIngredientRepository.findAll();
    }

    // READ food ingredient by ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodIngredient> getFoodIngredientById(@PathVariable String id) {
        Optional<FoodIngredient> foodIngredient = foodIngredientRepository.findById(id);
        if (foodIngredient.isPresent()) {
            return ResponseEntity.ok(foodIngredient.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE food ingredient
    @PutMapping("/{id}")
    public ResponseEntity<FoodIngredient> updateFoodIngredient(@PathVariable String id, @RequestBody FoodIngredient foodIngredient) {
        Optional<FoodIngredient> existingFoodIngredient = foodIngredientRepository.findById(id);
        if (existingFoodIngredient.isPresent()) {
            foodIngredient.setId(id);
            FoodIngredient updatedFoodIngredient = foodIngredientRepository.save(foodIngredient);
            return ResponseEntity.ok(updatedFoodIngredient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE food ingredient by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodIngredient(@PathVariable String id) {
        Optional<FoodIngredient> foodIngredient = foodIngredientRepository.findById(id);
        if (foodIngredient.isPresent()) {
            foodIngredientRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // READ food ingredient by name
    @GetMapping("/name/{name}")
    public ResponseEntity<FoodIngredient> getFoodIngredientByName(@PathVariable String name) {
        Optional<FoodIngredient> foodIngredient = Optional.ofNullable(foodIngredientRepository.findByName(name));
        if (foodIngredient.isPresent()) {
            return ResponseEntity.ok(foodIngredient.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
