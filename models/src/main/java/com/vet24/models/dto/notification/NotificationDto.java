package com.vet24.models.dto.notification;

import com.vet24.models.enums.notification.Summary;
import com.vet24.models.user.User;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class NotificationDto {
    Summary summary;
    String description;
    Timestamp startDate;
    Timestamp endDate;
    List<User> listUser;
}
