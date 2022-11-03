package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.Appointment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AppointmentDaoImpl extends ReadWriteDaoImpl<Long, Appointment> implements AppointmentDao {

    @Override
    public List<LocalDateTime> getLocalDateTimeByDoctorIdAndBetweenDates(Long doctorId, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
        return manager
                .createQuery("select d.startDateTime from Appointment d WHERE d.doctor.id = :id AND d.startDateTime >= :dateTimeStart AND d.startDateTime <= :dateTimeEnd", LocalDateTime.class)
                .setParameter("id", doctorId)
                .setParameter("dateTimeStart", dateTimeStart)
                .setParameter("dateTimeEnd", dateTimeEnd)
                .getResultList();
    }

}
