package com.help.help_backend.service;

import com.help.help_backend.dto.RegisterRequest;
import com.help.help_backend.entity.User;
import com.help.help_backend.repository.UserRepository;
import com.help.help_backend.util.DistanceCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor-based dependency injection
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Validate phone number format
    private boolean isValidPhoneNumber(String phone) {
        String regex = "^\\+?[1-9]\\d{1,14}$";  // E.164 phone number format
        return Pattern.matches(regex, phone);
    }

    // Validate password strength
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && // At least 8 characters
                password.matches(".*[A-Z].*") && // Contains at least one uppercase letter
                password.matches(".*[a-z].*") && // Contains at least one lowercase letter
                password.matches(".*\\d.*");    // Contains at least one digit
    }

    // Validate latitude
    private boolean isValidLatitude(double latitude) {
        return latitude >= -90 && latitude <= 90;
    }

    // Validate longitude
    private boolean isValidLongitude(double longitude) {
        return longitude >= -180 && longitude <= 180;
    }

    // User registration
    public User registerUser(RegisterRequest registerRequest) {
        // Validate username uniqueness
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("The username is already taken. Please choose another.");
        }

        // Validate phone number
        if (!isValidPhoneNumber(registerRequest.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format. Use the international format.");
        }

        // Validate password strength
        if (!isValidPassword(registerRequest.getPassword())) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters long and include at least one uppercase letter, " +
                            "one lowercase letter, and one number."
            );
        }

        // Validate latitude and longitude
        if (!isValidLatitude(registerRequest.getLatitude()) || !isValidLongitude(registerRequest.getLongitude())) {
            throw new IllegalArgumentException("Invalid latitude or longitude values.");
        }

        // Assign default role if not provided
        User.Role role = registerRequest.getRole() != null ? registerRequest.getRole() : User.Role.USER;

        // Restrict assigning ADMIN role during registration
        if (role == User.Role.ADMIN) {
            throw new IllegalArgumentException("Admin role cannot be assigned during registration.");
        }

        // Check for valid roles
        if (!EnumSet.of(User.Role.USER, User.Role.HELPER).contains(role)) {
            throw new IllegalArgumentException("Invalid role. Only USER or HELPER roles are allowed.");
        }

        // Create and save the user
        User user = new User();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setLatitude(registerRequest.getLatitude());
        user.setLongitude(registerRequest.getLongitude());
        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }

    // Find helpers near a location
    @Cacheable("nearbyHelpers")
    public List<User> findAvailableHelpers(double latitude, double longitude, double radius) {
        // Retrieve all helpers
        List<User> allHelpers = userRepository.findByRolesContaining(User.Role.HELPER);
        List<User> nearbyHelpers = new ArrayList<>();

        // Check the distance for each helper
        for (User helper : allHelpers) {
            double distance = DistanceCalculator.calculateDistance(
                    latitude, longitude, helper.getLatitude(), helper.getLongitude()
            );
            if (distance <= radius) {
                nearbyHelpers.add(helper);
            }
        }

        return nearbyHelpers;
    }
}
