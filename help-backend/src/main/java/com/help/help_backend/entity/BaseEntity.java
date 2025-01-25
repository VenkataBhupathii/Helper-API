package com.help.help_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @CreatedDate
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "last_updated_date_time")
    private LocalDateTime lastUpdatedDateTime;

    @PrePersist
    public void init() {
        id = UUID.randomUUID().toString();
        createdDateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdatedDateTime = LocalDateTime.now();
    }
}
