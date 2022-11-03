package com.vet24.service.medicine.dto;

import com.vet24.models.dto.appointment.AppointmentCalendarElementDto;
import com.vet24.models.dto.appointment.AppointmentCallendarDto;
import com.vet24.models.dto.appointment.AppointmentDayElementDto;
import com.vet24.models.medicine.DoctorSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentCalendarDtoService {

    //List<AppointmentCalendarElementDto> doctorScheduleNotFound(LocalDate dateDoctor);
    AppointmentCallendarDto createAppointmentCalendarDtoWithoutDoctorId(LocalDate firstDayOfWeek);

    AppointmentCallendarDto createAppointmentCalendarDto(Long doctorId, LocalDate firstDayOfWeek);

}
