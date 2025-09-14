package com.example.carrental.dto;

import com.example.carrental.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String firstName;
    private String lastName;
    private String address;
    private String avatar;
    private boolean enabled;
    private Set<Role> roles;
    private LocalDateTime createdAt;
}