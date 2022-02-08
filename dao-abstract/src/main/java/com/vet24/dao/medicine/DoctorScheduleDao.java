package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.medicine.DoctorSchedule;

import java.time.LocalDate;

public interface DoctorScheduleDao extends ReadWriteDao<Long, DoctorSchedule> {

    boolean isExistByDoctorIdAndWeekNumber(Long doctorId, LocalDate startWeek);
}
