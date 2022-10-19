package com.vet24.service.medicine.dto;

import com.vet24.models.dto.appointment.AppointmentCalendarElementDto;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentCalendarDtoService {

    List<AppointmentCalendarElementDto> createAppointmentCalendarDto(Long doctorId, LocalDate dateDoctor, LocalDate dateRequest);
}
