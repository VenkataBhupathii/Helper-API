package com.help.help_backend.service;

import com.help.help_backend.dto.RegisterRequest;
import com.help.help_backend.dto.AuthRequest;
import com.help.help_backend.entity.User;
import com.help.help_backend.entity.User.Role;
import com.help.help_backend.exception.InvalidCredentialsException;
import com.help.help_backend.exception.UserNotFoundException;
import com.help.help_backend.repository.UserRepository;
import com.help.help_backend.config.JwtUtil;
import com.help.help_backend.util.DistanceCalculator; // Import the utility class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }




    // User authentication
    public String authenticateUser(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    // Extract user from JWT token
    public User getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer " prefix
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for token: " + token));
    }




    // Send reset password email
    public void sendPasswordResetEmail(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String resetToken = jwtUtil.generatePasswordResetToken(user.getUsername());
        // Logic to send reset email with the token link

        String resetLink = "https://yourdomain.com/reset-password?token=" + resetToken;
        System.out.println("Reset Password Link: " + resetLink);


        emailService.sendPasswordResetEmail(user.getUsername(), resetLink);

    }

    // Validate reset token and reset the password
    public void resetPassword(String token, String newPassword) {

        if (jwtUtil.isTokenExpired(token)) {
            throw new InvalidCredentialsException("Reset token is expired");
        }

        String username = jwtUtil.extractUsername(token); // Extract username from token

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}
