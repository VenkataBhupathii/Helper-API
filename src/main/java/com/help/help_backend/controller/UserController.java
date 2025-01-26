package com.help.help_backend.controller;

import com.help.help_backend.dto.RegisterRequest;
import com.help.help_backend.entity.User;
import com.help.help_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/helpers")
    public ResponseEntity<List<User>> findHelpers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        List<User> helpers = userService.findAvailableHelpers(latitude, longitude, radius);
        return ResponseEntity.ok(helpers);
    }

}
