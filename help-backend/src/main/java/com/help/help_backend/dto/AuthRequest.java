package com.help.help_backend.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username; // Username for login
    private String password; // Password for login
}
