package com.vet24.models.mappers.dto;

import com.vet24.models.mappers.notification.NotificationMapper;
import com.vet24.models.mappers.notification.UserNotificationMapper;
import com.vet24.models.mappers.user.UserInfoMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {NotificationMapper.class, UserInfoMapper.class})
public interface UserNotificationDtoMapper extends UserNotificationMapper {
}
