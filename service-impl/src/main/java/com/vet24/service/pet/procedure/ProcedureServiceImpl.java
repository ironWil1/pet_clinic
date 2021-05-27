package com.vet24.service.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.procedure.ProcedureDao;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.notification.Notification;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.service.ReadWriteServiceImpl;
import com.vet24.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ProcedureServiceImpl extends ReadWriteServiceImpl<Long, Procedure> implements ProcedureService {

    private final ProcedureDao procedureDao;
    private final NotificationService notificationService;

    @Autowired
    public ProcedureServiceImpl(ReadWriteDaoImpl<Long, Procedure> readWriteDao, ProcedureDao procedureDao,
                                NotificationService notificationService) {
        super(readWriteDao);
        this.procedureDao = procedureDao;
        this.notificationService = notificationService;
    }

    @Override
    public void persist(Procedure entity) {
        if (entity.getIsPeriodical()) {
            persistProcedureNotification(entity);
        }

        super.persist(entity);
    }

    @Override
    public Procedure update(Procedure entity) {
        Procedure oldEntity = super.getByKey(entity.getId());

        // old(not periodical) + new(periodical) -> create notification & event
        if (!oldEntity.getIsPeriodical() && entity.getIsPeriodical()) {
            persistProcedureNotification(entity);
        }
        // old(periodical) + new(periodical) -> update notification & event
        if (oldEntity.getIsPeriodical() && entity.getIsPeriodical()) {
            updateProcedureNotification(oldEntity, entity);
        }
        // old(periodical) + new(not periodical) -> delete notification & event
        if (oldEntity.getIsPeriodical() && !entity.getIsPeriodical()) {
            deleteProcedureNotification(oldEntity);
        }

        return super.update(entity);
    }

    @Override
    public void delete(Procedure entity) {
        if (entity.getIsPeriodical()) {
            deleteProcedureNotification(entity);
        }

        super.delete(entity);
    }

    private void persistProcedureNotification(Procedure entity) {
        if (!(entity.getPeriodDays() > 0)) {
            throw new BadRequestException("for periodical procedure need to set period days");
        }
        Pet pet = entity.getPet();
        Notification notification = new Notification(
                null,
                null,
                Timestamp.valueOf(LocalDateTime.of(entity.getDate().plusDays(entity.getPeriodDays()), LocalTime.MIDNIGHT)),
                Timestamp.valueOf(LocalDateTime.of(entity.getDate().plusDays(entity.getPeriodDays()), LocalTime.NOON)),
                "Periodic procedure for your pet",
                "Pet clinic 1",
                "Procedure '" + entity.getType().name().toLowerCase() + "' \n" +
                        "for pet " + pet.getName() + " \n" +
                        "[every " + entity.getPeriodDays() + " day(s)]",
                pet
        );
        notificationService.persist(notification);

        entity.setNotification(notification);
        pet.addNotification(notification);
    }

    private void updateProcedureNotification(Procedure oldEntity, Procedure newEntity) {
        if (!(newEntity.getPeriodDays() > 0)) {
            throw new BadRequestException("for periodical procedure need to set period days");
        }
        Pet pet = oldEntity.getPet();
        Notification notification = new Notification(
                oldEntity.getNotification().getId(),
                oldEntity.getNotification().getEvent_id(),
                Timestamp.valueOf(LocalDateTime.of(newEntity.getDate().plusDays(newEntity.getPeriodDays()), LocalTime.MIDNIGHT)),
                Timestamp.valueOf(LocalDateTime.of(newEntity.getDate().plusDays(newEntity.getPeriodDays()), LocalTime.NOON)),
                "Periodic procedure for your pet",
                "Pet clinic 1",
                "Procedure '" + newEntity.getType().name().toLowerCase() + "' \n" +
                        "for pet " + pet.getName() + " \n" +
                        "[every " + newEntity.getPeriodDays() + " day(s)]",
                pet
        );

        notificationService.update(notification);
        newEntity.setNotification(notification);
    }

    private void deleteProcedureNotification(Procedure entity) {
        if (entity.getNotification() == null) {
            throw new BadRequestException("notification not found");
        }

        Notification notification = entity.getNotification();
        entity.setNotification(null);
        entity.getPet().removeNotification(notification);
        notificationService.delete(notification);
    }
}
