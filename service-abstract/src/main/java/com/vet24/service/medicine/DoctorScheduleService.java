package com.vet24.service.medicine;

import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface DoctorScheduleService extends ReadWriteService<Long, DoctorSchedule> {

    List<DoctorSchedule> getScheduleByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber);

    boolean doctorIsBusyAtWeekNumber(Long doctorId, Integer weekNumber);
}
