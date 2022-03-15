package com.vet24.models.mappers.notification;

import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.notification.UserNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNotificationMapper extends DtoMapper<UserNotification, UserNotificationDto>,
        EntityMapper<UserNotificationDto, UserNotification> {

}
