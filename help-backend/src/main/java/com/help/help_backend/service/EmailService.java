package com.help.help_backend.service;

import com.help.help_backend.entity.User;
import com.help.help_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email, String resetLink) {
        Optional<User> user = userRepository.findByUsername(email);

        if (user.isPresent()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset Request");
            message.setText("Click the link to reset your password: " + resetLink);
            mailSender.send(message);
        } else {
            throw new IllegalArgumentException("No user found with email: " + email);
        }
    }
}
