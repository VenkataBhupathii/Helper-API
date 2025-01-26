package com.help.help_backend.controller;

import com.help.help_backend.dto.AuthRequest;
import com.help.help_backend.dto.AuthResponse;
import com.help.help_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.authenticateUser(authRequest);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials"));
        }
    }

    // Reset password request endpoint
    @GetMapping("/password/reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String username) {
        try {
            authService.sendPasswordResetEmail(username);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or email sending failed.");
        }
    }

    // Reset password endpoint
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
}
