package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.DoctorSchedule;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorScheduleDaoImpl extends ReadWriteDaoImpl<Long, DoctorSchedule> implements DoctorScheduleDao {

    @Override
    public boolean isExistByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber) {
        return manager
                .createQuery("SELECT CASE WHEN (count(id)>0) then true else false end" +
                        " FROM DoctorSchedule WHERE doctor.id = :doctorId AND weekNumber = :weekNumber", Boolean.class)
                .setParameter("doctorId", doctorId)
                .setParameter("weekNumber", weekNumber)
                .getSingleResult();
    }
}
