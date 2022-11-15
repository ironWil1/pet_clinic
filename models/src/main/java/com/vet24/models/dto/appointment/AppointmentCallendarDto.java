package com.vet24.models.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AppointmentCallendarDto {
    private List<AppointmentCalendarElementDto> days;
}
