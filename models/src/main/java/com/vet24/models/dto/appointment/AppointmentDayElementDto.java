package com.vet24.models.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AppointmentDayElementDto {

    private LocalTime time;
    private boolean isAvailable;
}
