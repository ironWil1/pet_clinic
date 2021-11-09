package com.vet24.models.dto.user;

import com.vet24.models.enums.DayOffType;
import com.vet24.models.user.Doctor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorNonWorkingDto {

    private Long Id;
    private Doctor doctor;
    private DayOffType type;
    private LocalDate date;
}