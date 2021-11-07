package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.medicine.DoctorSchedule;

import java.util.List;

public interface DoctorScheduleDao extends ReadWriteDao<Long, DoctorSchedule> {

    List<DoctorSchedule> getScheduleByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber);
}
