package com.vet24.service.medicine;

import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteService;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService extends ReadWriteService<Long, DoctorSchedule> {

    boolean isExistByDoctorIdAndWeekNumber(Long doctorId, LocalDate startWeek);
    public List<DoctorSchedule> getDoctorScheduleAfterDate(LocalDate date);
    String getDoctorScheduleWorkShift(Long doctorId, LocalDate date);
    List<DoctorSchedule> getDoctorScheduleCurrentDate(LocalDate date);
    Long getDoctorId(DoctorSchedule doctorSchedule);
}
