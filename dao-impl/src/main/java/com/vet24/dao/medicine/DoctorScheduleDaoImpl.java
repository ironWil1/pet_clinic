package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.enums.WorkShift;
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

    @Override
    public WorkShift getDoctorScheduleWorkShift(Long doctorId, LocalDate date) {
        return manager
                .createQuery("select d.workShift FROM DoctorSchedule d WHERE d.doctor.id = :id AND d.startWeek = :date", WorkShift.class)
                .setParameter("id", doctorId)
                .setParameter("date", date)
                .getSingleResult();
    }


    @Override
    public List<DoctorSchedule> getDoctorScheduleCurrentDate(LocalDate date) {
        return manager
                .createQuery("FROM DoctorSchedule d WHERE d.startWeek = :date", DoctorSchedule.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public Long getDoctorId(DoctorSchedule doctorSchedule) {
        Long id = doctorSchedule.getId();
        return manager
                .createQuery("select d.doctor.id FROM DoctorSchedule d WHERE d.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
