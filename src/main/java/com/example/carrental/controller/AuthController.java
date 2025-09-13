package com.example.carrental.controller;

import com.example.carrental.dto.UpdateUserDto;
import com.example.carrental.dto.UserDto;
import com.example.carrental.entity.User;
import com.example.carrental.mapper.UserMapper;
import com.example.carrental.repository.UserRepository;
import com.example.carrental.service.UserService;
import com.example.carrental.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v2/auth")
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
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setMobile(userDto.getMobile());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setAvatar(userDto.getAvatar());
        user.setEnabled(false); // User is inactive until email is verified

        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        User user = userService.authenticate(userDto.getUsername(), userDto.getPassword());
        if (user != null) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/user")
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
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // Map to DTO to avoid exposing password
            UserDto userDto = UserMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token: " + e.getMessage());
        }
    }

    // ----------------- Update User -----------------
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token,
                                        @RequestBody UpdateUserDto updateUserDto) {

        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User updatedUser = userService.updateUser(username, updateUserDto);

        return ResponseEntity.ok("User updated successfully!");
    }

    // ----------------- Delete User -----------------
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {

        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        userService.deleteUser(username);

        return ResponseEntity.ok("User deleted successfully!");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean verified = userService.verifyEmail(token);

        if (verified) {
            return ResponseEntity.ok("Email verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }


}