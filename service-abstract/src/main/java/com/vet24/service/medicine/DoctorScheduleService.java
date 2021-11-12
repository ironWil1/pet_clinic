package com.vet24.service.medicine;

import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteService;

public interface DoctorScheduleService extends ReadWriteService<Long, DoctorSchedule> {

    boolean isExistByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber);
}
