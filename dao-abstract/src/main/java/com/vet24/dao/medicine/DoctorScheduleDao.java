package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.medicine.DoctorSchedule;

public interface DoctorScheduleDao extends ReadWriteDao<Long, DoctorSchedule> {

    boolean isExistByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber);
}
