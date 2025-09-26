package com.example.carrental.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
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
    private Set<String> roles;
    private LocalDateTime createdAt;
}