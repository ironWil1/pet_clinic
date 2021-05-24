package com.vet24.models.mappers.notification;

import com.vet24.models.dto.googleEvent.GoogleEventDto;
import com.vet24.models.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationEventMapper {

    @Mapping(source = "event_id", target = "id")
    GoogleEventDto notificationToGoogleEventDto(Notification notification);

    default GoogleEventDto notificationWithEmailToGoogleEventDto(Notification notification, String email) {
        GoogleEventDto dto = this.notificationToGoogleEventDto(notification);
        dto.setEmail(email);

        return dto;
    }
}
