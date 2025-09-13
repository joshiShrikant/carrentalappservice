package com.example.carrental.mapper;

import com.example.carrental.dto.UserDto;
import com.example.carrental.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setAddress(user.getAddress());
            dto.setAvatar(user.getAvatar());
            dto.setEnabled(user.isEnabled());
            dto.setRoles(user.getRoles());
            dto.setCreatedAt(user.getCreatedAt());
            return dto;
        }

        // New method to map from UserDto to User
        public static User toEntity(UserDto userDto) {
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

