package com.example.kyfbackend.payloads.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOnBoardData {
    private String id;
    private float weight;
    private float height;
    private int age;
    private String gender;
    private String activityLevel;
}
