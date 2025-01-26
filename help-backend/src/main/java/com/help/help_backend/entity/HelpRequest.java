package com.help.help_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class HelpRequest extends BaseEntity {

    private String description;
    private double latitude;
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // User needing help
    private User user;

    @ManyToOne
    @JoinColumn(name = "helper_id") // User providing help
    private User helper;


    public enum HelpRequestStatus {
        PENDING,
        ACCEPTED,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private HelpRequestStatus status;


}
