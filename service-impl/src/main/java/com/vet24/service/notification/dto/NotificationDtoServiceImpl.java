package com.vet24.service.notification.dto;

import com.vet24.dao.notification.dto.NotificationDtoDao;
import com.vet24.models.dto.notification.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationDtoServiceImpl implements NotificationDtoService{

    @Autowired
    private final NotificationDtoDao notificationDtoDao;

    public NotificationDtoServiceImpl(NotificationDtoDao notificationDtoDao) {
        this.notificationDtoDao = notificationDtoDao;
    }


    @Override
    public List<NotificationDto> getEmailsAndContentsForNotifications(LocalDate eventDate) {
        return notificationDtoDao.getEmailsAndContentsForNotifications(eventDate);
    }

    @Override
    public List<Long> getIdFromNotificationDtoList(List<NotificationDto> notificationDtoList) {
        return notificationDtoList.stream().map(NotificationDto::getId).collect(Collectors.toList());
    }
}
