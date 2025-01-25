package com.help.help_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HelpRequestDto {
    private String description;
    private double latitude;
    private double longitude;
    private Long userId; // User who needs help (we only send the user's ID)
    private Long helperId; // Helper who will assist (we send the helper's ID if assigned)
    private String status; // 'PENDING', 'ACCEPTED', 'COMPLETED'
    private Date createdAt;
    private Date updatedAt;
}
