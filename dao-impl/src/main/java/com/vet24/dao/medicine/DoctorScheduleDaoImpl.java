package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.DoctorSchedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class DoctorScheduleDaoImpl extends ReadWriteDaoImpl<Long, DoctorSchedule> implements DoctorScheduleDao {

    @Override
    public boolean isExistByDoctorIdAndWeekNumber(Long doctorId, LocalDate startWeek) {
        return manager
                .createQuery("SELECT CASE WHEN (count(id)>0) then true else false end" +
                        " FROM DoctorSchedule WHERE doctor.id = :doctorId AND startWeek = :startWeek", Boolean.class)
                .setParameter("doctorId", doctorId)
                .setParameter("startWeek", startWeek)
                .getSingleResult();
    }
}
