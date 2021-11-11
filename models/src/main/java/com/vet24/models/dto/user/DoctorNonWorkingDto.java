package com.vet24.models.dto.user;

import com.vet24.models.enums.DayOffType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DoctorNonWorkingDto {

    private Long doctorNonWorkingId;
    private Long doctorId;
    private DayOffType type;
    private LocalDate date;
}
