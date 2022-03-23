package com.vet24.models.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {

    private Long id;
    private String content;
    private boolean isImportant;
}
