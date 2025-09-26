package com.example.carrental.service;

import com.example.carrental.dto.UserDTO;
import com.example.carrental.entity.Role;
import com.example.carrental.entity.User;
import com.example.carrental.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisUserService {

    private final UserRepository userRepository;

    public RedisUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users_all")
    public List<UserDTO> getAllUsers() {
        System.out.println("👉 Fetching all users from DB...");

        List<User> users = userRepository.findAll();

        // Map to DTO
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private UserDTO mapToDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobile(user.getMobile())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                // 🚀 Convert roles to simple String list
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }


    @Cacheable(value = "user_by_id", key = "#id")
    public UserDTO getUserById(Long id) {
        System.out.println("👉 Fetching user by ID from DB...");
        return userRepository.findById(id)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .mobile(user.getMobile())
                        .address(user.getAddress())
                        .avatar(user.getAvatar())
                        .roles(
                                user.getRoles().stream()
                                        .map(Role::getName) // ✅ convert Role → String
                                        .collect(Collectors.toSet()) // ✅ Set<String>
                        )
                        .build())
                .orElse(null);
    }

    public UserDTO saveUser(User user) {
        return mapToDTO(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
