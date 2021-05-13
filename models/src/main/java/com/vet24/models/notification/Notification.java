package com.vet24.models.notification;

import com.vet24.models.enums.Summary;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private Summary summary;

    private String description;

    private Timestamp startDate;
    private Timestamp endDate;

    public Notification(String email, Summary summary, String description, Timestamp startDate, Timestamp endDate) {
        this.email = email;
        this.summary = summary;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
