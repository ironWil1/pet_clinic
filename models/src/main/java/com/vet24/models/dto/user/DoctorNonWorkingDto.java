package com.vet24.models.dto.user;

import com.vet24.models.enums.DayOffType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class DoctorNonWorkingDto {
    @NotNull(message = "{doctorNonWorking.validation.blank.field}")
    private Long doctorNonWorkingId;
    @NotNull(message = "{doctorNonWorking.validation.blank.field}")
    private Long doctorId;
    @NotNull(message = "{doctorNonWorking.validation.blank.field}")
    private DayOffType type;
    @NotNull(message = "{doctorNonWorking.validation.blank.field}")
    private LocalDate date;
}
