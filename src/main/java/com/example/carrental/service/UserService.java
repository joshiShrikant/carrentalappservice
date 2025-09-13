package com.example.carrental.service;

import com.example.carrental.dto.UpdateUserDto;
import com.example.carrental.entity.EmailVerificationToken;
import com.example.carrental.entity.Role;
import com.example.carrental.entity.User;
//import com.example.carrental.model.Role;
import com.example.carrental.repository.EmailVerificationTokenRepository;
import com.example.carrental.repository.RoleRepository;
import com.example.carrental.repository.UserRepository;
import com.example.carrental.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final EmailVerificationTokenRepository tokenRepository;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       JwtUtil jwtUtil, EmailService emailService,
                       EmailVerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public User registerUser(User userRequest) {

        User user = new User();

        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword())); // encode password
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setAddress(userRequest.getAddress());
        user.setAvatar(userRequest.getAvatar());
        user.setMobile(userRequest.getMobile());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        // Assign default role as USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24-hour expiry

        tokenRepository.save(verificationToken);
        // Send email with verification link
        String link = "http://localhost:8090/v2/auth/verify?token=" + token;
        emailService.sendEmail(user.getEmail(), "Email Verification", "Click the link to verify: " + link);
        return savedUser;
    }

    public User updateUser(String username, UpdateUserDto updateUserDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getFirstName() != null) user.setFirstName(updateUserDto.getFirstName());
        if (updateUserDto.getLastName() != null) user.setLastName(updateUserDto.getLastName());
        if (updateUserDto.getAddress() != null) user.setAddress(updateUserDto.getAddress());
        if (updateUserDto.getAvatar() != null) user.setAvatar(updateUserDto.getAvatar());

        return userRepository.save(user);
    }

    // ----------------- Delete User -----------------
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
    public boolean verifyEmail(String token) {
        var verificationToken = tokenRepository.findByToken(token).orElse(null);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true); // Activate user
        userRepository.save(user);
        tokenRepository.delete(verificationToken); // Optional: clean up token
        return true;
    }

    public User authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }
}