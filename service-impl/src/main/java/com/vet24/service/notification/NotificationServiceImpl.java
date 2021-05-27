package com.vet24.service.notification;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.notification.NotificationDao;
import com.vet24.models.dto.googleEvent.GoogleEventDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.notification.NotificationEventMapper;
import com.vet24.models.notification.Notification;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationServiceImpl extends ReadWriteServiceImpl<Long, Notification> implements NotificationService {

    private final NotificationDao notificationDao;
    private final NotificationEventMapper notificationEventMapper;
    private final GoogleEventService googleEventService;

    @Autowired
    public NotificationServiceImpl(ReadWriteDaoImpl<Long, Notification> readWriteDao, NotificationDao notificationDao,
                                   NotificationEventMapper notificationEventMapper, GoogleEventService googleEventService) {
        super(readWriteDao);
        this.notificationDao = notificationDao;
        this.notificationEventMapper = notificationEventMapper;
        this.googleEventService = googleEventService;
    }

    @Override
    public void persist(Notification entity) {
        GoogleEventDto googleEventDto = notificationEventMapper
                .notificationToGoogleEventDto(entity, entity.getPet().getClient().getEmail());

        try {
            googleEventService.createEvent(googleEventDto);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage(), exception.getCause());
        }
        entity.setEvent_id(googleEventDto.getId());

        super.persist(entity);
    }

    @Override
    public Notification update(Notification entity) {
        GoogleEventDto googleEventDto = notificationEventMapper
                .notificationToGoogleEventDto(entity, entity.getPet().getClient().getEmail());

        try {
            googleEventService.editEvent(googleEventDto);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage(), exception.getCause());
        }

        return super.update(entity);
    }

    @Override
    public void delete(Notification entity) {
        GoogleEventDto googleEventDto = new GoogleEventDto();
        googleEventDto.setId(entity.getEvent_id());
        googleEventDto.setEmail(entity.getPet().getClient().getEmail());

        try {
            googleEventService.deleteEvent(googleEventDto);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage(), exception.getCause());
        }

        super.delete(entity);
    }
}
