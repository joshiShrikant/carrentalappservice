package com.example.carrental.dto;

import com.example.carrental.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserRequestDto {
    private Long id;
    private String username;
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
