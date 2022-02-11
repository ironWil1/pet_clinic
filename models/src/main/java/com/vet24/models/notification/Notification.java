package com.vet24.models.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private LocalDate eventDate;

    private boolean isImportant;

    @OneToMany(
            mappedBy = "notification",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserNotification> userNotifications = new ArrayList<>();


    public Notification(String content, LocalDate eventDate, boolean isImportant) {
        this.content = content;
        this.eventDate = eventDate;
        this.isImportant = isImportant;
    }
}
