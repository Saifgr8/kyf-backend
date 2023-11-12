package com.example.kyfbackend.controllers;

import com.example.kyfbackend.models.Recipe;
import com.example.kyfbackend.repos.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    @Autowired private RecipeRepository recipeRepository;

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/owner/{id}")
    public List<Recipe> getRecipesByOwner(@PathVariable String id) {
        return recipeRepository.findByOwner(id);
    }

    @PostMapping
    public String createRecipe(@RequestBody Recipe recipe) {
        Recipe existingRecipe = recipeRepository.findByName(recipe.getName());
        if (existingRecipe != null) {
            return "Recipe already exists";
        }
        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipe.getName());
        newRecipe.setDescription(recipe.getDescription());
        newRecipe.setOwner(recipe.getOwner());
        newRecipe.setIngredients(recipe.getIngredients());
        newRecipe.setMacros(recipe.getMacros());
        newRecipe.setCreatedAt(LocalDate.now());
        newRecipe.setUpdatedAt(LocalDate.now());
        recipeRepository.save(newRecipe);
        return "New recipe created";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody Recipe recipe) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            Recipe updatedRecipe = recipeRepository.save(recipe);
            return ResponseEntity.ok(updatedRecipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            recipeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
