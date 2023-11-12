package com.example.kyfbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFoodItem {
    private String name;
    private float amount;
    private Macros macros;
    private String type;
}
