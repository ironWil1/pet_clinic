package com.vet24.models.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class AppointmentCalendarElementDto {
    private LocalDate date;
    private List<AppointmentDayElementDto> appointments;
}
