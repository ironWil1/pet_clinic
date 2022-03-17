package com.vet24.models.mappers.notification;

import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.notification.Notification;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NotificationMapper extends DtoMapper<Notification, NotificationDto>,
        EntityMapper<NotificationDto, Notification> {

}
