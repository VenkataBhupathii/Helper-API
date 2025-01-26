package com.help.help_backend.dto;

import com.help.help_backend.entity.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;         // Full name of the user
    private String username;     // Unique username
    private String password;     // Password (to be encrypted)
    private String phone;        // Contact number
    private double latitude;     // User's current latitude (optional)
    private double longitude;    // User's current longitude (optional)
    private User.Role role;
}
