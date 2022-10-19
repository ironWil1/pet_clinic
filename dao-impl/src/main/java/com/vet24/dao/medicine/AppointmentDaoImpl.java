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
    public boolean isExistByDoctorIdAndLocalDateTime(Long doctorId, LocalDateTime dateTime) {
        return manager
                .createQuery("SELECT CASE WHEN (count(id)>0) then true else false end" +
                        " FROM Appointment WHERE doctor.id = :doctorId AND startDateTime = :start", Boolean.class)
                .setParameter("doctorId", doctorId)
                .setParameter("start", dateTime)
                .getSingleResult();
    }
}
