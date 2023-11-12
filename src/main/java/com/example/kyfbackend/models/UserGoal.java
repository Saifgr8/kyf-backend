package com.example.kyfbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserGoal {
    @Field("diet_type")
    private String dietType;
    private float calories;
    private float protein;
    private float carbs;
    private float fat;
    private float sugars;
    private float fiber;
}
