package com.vet24.models.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AppointmentDayElementDto implements Comparable<AppointmentDayElementDto> {

    private LocalTime time;
    private boolean isAvailable;

    @Override
    public int compareTo(AppointmentDayElementDto o) {
        if (this.time.equals(o.time)) {
            return 0;
        } else if (this.time.isBefore(o.time)) {
            return -1;
        } else {
            return 1;
        }
    }
}
