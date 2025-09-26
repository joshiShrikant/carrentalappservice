package com.example.carrental.controller;

import com.example.carrental.dto.JwtResponse;
import com.example.carrental.dto.LoginRequestDto;
import com.example.carrental.dto.UpdateUserDto;
import com.example.carrental.dto.UserDTO;
import com.example.carrental.entity.User;
import com.example.carrental.mapper.UserMapper;
import com.example.carrental.repository.UserRepository;
import com.example.carrental.service.UserService;
import com.example.carrental.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("api/v2/auth")
@Tag(name = "Auth", description = "Auth management APIs")
public class AuthController {

    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "register User", description = "register a User on app")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be empty");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setMobile(userDto.getMobile());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setAvatar(userDto.getAvatar());
        user.setPassword(userDto.getPassword());
        user.setEnabled(false); // User is inactive until email is verified

        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "login User", description = "login a User on app")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto userDto) {
        User user = userService.authenticate(userDto.getUsername(), userDto.getPassword());
        user.setPassword("");
        if (user != null) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new JwtResponse(user.getUsername(), token, "Bearer", user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/user")
    @Operation(summary = "get User", description = "get a User on app")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        try {
            // Extract token from Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7); // Remove "Bearer "

            // Extract username from token
            String username = jwtUtil.extractUsername(token);

            // Lookup user in database
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            // Map to DTO to avoid exposing password
            UserDTO userDto = UserMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token: " + e.getMessage());
        }
    }

    // ----------------- Update User -----------------
    @PutMapping("/update")
    @Operation(summary = "update User", description = "update a User on app")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @RequestBody UpdateUserDto updateUserDto) {

        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User updatedUser = userService.updateUser(username, updateUserDto);

        return ResponseEntity.ok("User updated successfully!");
    }

    // ----------------- Delete User -----------------
    @DeleteMapping("/delete")
    @Operation(summary = "delete User", description = "delete a User on app")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {

        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        userService.deleteUser(username);

        return ResponseEntity.ok("User deleted successfully!");
    }

    @GetMapping("/verify")
    @Operation(summary = "verify Email", description = "verify a User on app")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean verified = userService.verifyEmail(token);

        if (verified) {
            return ResponseEntity.ok("Email verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    @PutMapping("/updateProfile")
    @Operation(summary = "update Profile", description = "update a Profile on app")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UpdateUserDto updateUserDto) {

        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User updatedUser = userService.updateUser(username, updateUserDto);

        Map<String, Object> response = Map.of("status", 200, "msg", "Profile updated successfully!", "user", updatedUser);
        return ResponseEntity.ok(response);
    }

}