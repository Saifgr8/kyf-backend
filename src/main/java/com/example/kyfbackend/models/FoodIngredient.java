package com.example.kyfbackend.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "food_ingredients")
public class FoodIngredient {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String unit;
    private int amount;
    private float calories;
    private float carbohydrates;
    private float fat;
    private float protein;
    private float sugars;
    private float fiber;

    private String type;
}

