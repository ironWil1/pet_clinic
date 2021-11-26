package com.vet24.models.dto.user;

import com.vet24.models.enums.DayOffType;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DoctorNonWorkingDto {
    private Long doctorNonWorkingId;
    @NotNull(message = "{doctorNonWorking.validation}")
    private Long doctorId;
    @NotNull(message = "{doctorNonWorking.validation}")
    private DayOffType type;
    @NotNull(message = "{doctorNonWorking.validation}")
    private LocalDate date;
}
