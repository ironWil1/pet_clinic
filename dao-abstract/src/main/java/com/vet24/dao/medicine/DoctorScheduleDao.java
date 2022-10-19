package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleDao extends ReadWriteDao<Long, DoctorSchedule> {

    boolean isExistByDoctorIdAndWeekNumber(Long doctorId, LocalDate startWeek);
    public List<DoctorSchedule> getDoctorScheduleAfterDate(LocalDate date);

    String getDoctorScheduleWorkShift(Long doctorId, LocalDate date);

    List<DoctorSchedule> getDoctorScheduleCurrentDate(LocalDate date);

    Long getDoctorId(DoctorSchedule doctorSchedule);
}
