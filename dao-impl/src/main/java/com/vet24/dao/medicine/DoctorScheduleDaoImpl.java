package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.DoctorSchedule;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoctorScheduleDaoImpl extends ReadWriteDaoImpl<Long, DoctorSchedule> implements DoctorScheduleDao {

    @Override
    public List<DoctorSchedule> getScheduleByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber) {
        try {
            return manager.createQuery("SELECT sch FROM DoctorSchedule sch WHERE sch.doctor.id =: doctorId AND sch.weekNumber =: weekNumber", DoctorSchedule.class)
                    .setParameter("doctorId", doctorId)
                    .setParameter("weekNumber", weekNumber).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
