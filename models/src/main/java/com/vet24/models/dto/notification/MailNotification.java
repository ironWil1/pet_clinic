package com.vet24.models.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailNotification {

    private Long id;
    private String content;
    private LocalDate eventDate;
    private String email;
}
