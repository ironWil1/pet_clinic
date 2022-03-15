package com.vet24.models.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {

    private Long id;
    private String content;
    private boolean isImportant;
}
