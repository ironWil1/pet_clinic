package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.user.DoctorNonWorking;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public List<DoctorSchedule> getDoctorScheduleAfterDate(LocalDate date) {
        return manager
                .createQuery("FROM DoctorSchedule d WHERE d.startWeek >= :date", DoctorSchedule.class)
                .setParameter("date", date)
                .getResultList();
    }
}
