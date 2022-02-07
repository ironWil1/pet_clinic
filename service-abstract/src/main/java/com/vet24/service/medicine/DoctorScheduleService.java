package com.vet24.service.medicine;

import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteService;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService extends ReadWriteService<Long, DoctorSchedule> {

    boolean isExistByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber);
    List<DoctorSchedule> getDoctorScheduleListAfterWeekNumber(Integer weekNumber);
}
