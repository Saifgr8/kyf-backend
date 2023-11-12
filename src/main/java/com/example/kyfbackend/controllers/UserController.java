package com.example.kyfbackend.controllers;

import java.util.List;

import com.example.kyfbackend.models.User;
import com.example.kyfbackend.models.UserGoal;
import com.example.kyfbackend.payloads.request.UserOnBoardData;
import com.example.kyfbackend.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    // CREATE a new user
    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // READ all users
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    // UPDATE user by ID
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @PostMapping("/onboard")
    public User onBoard(@RequestBody UserOnBoardData userOnBoardData) {
        User user = userRepository.findById(userOnBoardData.getId()).orElse(null);
        user.setWeight(userOnBoardData.getWeight());
        user.setHeight(userOnBoardData.getHeight());
        user.setAge(userOnBoardData.getAge());
        user.setGender(userOnBoardData.getGender());
        user.setActivityLevel(userOnBoardData.getActivityLevel());
        return userRepository.save(user);
    }

    @PostMapping("/setgoal/{id}")
    public User setGoal(@PathVariable String id, @RequestBody UserGoal userGoal) {
        User user = userRepository.findById(id).orElse(null);
        user.setUserGoal(userGoal);
        return userRepository.save(user);
    }

    // READ user by name
    @GetMapping("/name/{username}")
    public User getUserByName(@PathVariable String name) {
        return userRepository.findByUsername(name);
    }
    // DELETE user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
    }
}