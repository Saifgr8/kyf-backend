package com.example.kyfbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recipes")
public class Recipe {
    @Id
    private String id;
    private String name;
    private String description;
    private String owner;
    private List<RecipeFoodItem> ingredients;
    private Macros macros;
    // created_at, updated_at

    @Field("created_at")
    private LocalDate createdAt;
    @Field("updated_at")
    private LocalDate updatedAt;
}