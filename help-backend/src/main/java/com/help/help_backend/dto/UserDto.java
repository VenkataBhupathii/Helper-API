package com.help.help_backend.dto;

import com.help.help_backend.entity.User;
import lombok.Data;

@Data
public class UserDto {

    private String name;
    private String username;
    private String password;
    private String phone;
    private User.Role role;
    private double latitude;
    private double longitude;
}
