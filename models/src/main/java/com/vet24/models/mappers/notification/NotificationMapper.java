package com.vet24.models.mappers.notification;


import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.Notification;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto notificationToNotificationDto(Notification notification);

    Notification notificationDtoToNotification(NotificationDto notificationDto);
}
