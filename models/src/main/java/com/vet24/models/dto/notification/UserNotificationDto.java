package com.vet24.models.dto.notification;

import com.vet24.models.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {

    private Long id;
    private UserInfoDto user;
    private NotificationDto notification;
    private boolean isShow;
}
