package com.help.help_backend.config;

import com.help.help_backend.entity.User;
import com.help.help_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Set;

@Component
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Changed from BCryptPasswordEncoder to PasswordEncoder

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:secureAdminPassword123}")
    private String adminPassword;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Automatically inject the PasswordEncoder
    }

    @PostConstruct
    public void initializeAdminUser() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setPhone("+10000000000"); // Replace with real data or leave empty
            admin.setLatitude(0.0); // Replace with meaningful data if necessary
            admin.setLongitude(0.0); // Replace with meaningful data if necessary
            admin.setRoles(Set.of(User.Role.ADMIN));
            userRepository.save(admin);
            System.out.println("Admin user created successfully.");
        }
    }
}
