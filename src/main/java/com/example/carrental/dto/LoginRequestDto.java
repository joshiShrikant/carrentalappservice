package com.example.carrental.dto;

import com.example.carrental.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}