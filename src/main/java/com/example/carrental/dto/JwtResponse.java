package com.example.carrental.dto;

import com.example.carrental.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String username;
    private String token;
    private String type = "Bearer";
    private User user;

//    private List<String> roles;
}
