package com.example.carrental.mapper;

import com.example.carrental.dto.UserDTO;
import com.example.carrental.entity.Role;
import com.example.carrental.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());

        // ✅ Convert Set<Role> → Set<String>
        if (user.getRoles() != null) {
            dto.setRoles(
                    user.getRoles().stream()
                            .map(Role::getName)   // assume Role has getName()
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

        // New method to map from UserDto to User
        public static User toEntity(UserDTO userDto) {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setAddress(userDto.getAddress());
            user.setAvatar(userDto.getAvatar());
            // Roles, enabled, createdAt can be set separately if needed
            return user;
        }
    }

