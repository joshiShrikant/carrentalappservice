package com.example.carrental.controller;

import com.example.carrental.dto.UserDTO;
import com.example.carrental.entity.User;
import com.example.carrental.service.RedisUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "User Management")
public class UserController {

    private final RedisUserService redisUserService;

    public UserController(RedisUserService redisUserService) {
        this.redisUserService = redisUserService;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserDTO> getAllUsers() {
        return redisUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public UserDTO getUserById(@PathVariable Long id) {
        return redisUserService.getUserById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public UserDTO createUser(@RequestBody User user) {
        return redisUserService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public void deleteUser(@PathVariable Long id) {
        redisUserService.deleteUserById(id);
    }
}
