package com.example.carrental.dto;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String avatar;
}
