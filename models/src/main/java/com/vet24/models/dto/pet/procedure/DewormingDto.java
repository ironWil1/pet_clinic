package com.vet24.models.dto.pet.procedure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.ProcedureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class DewormingDto {
    LocalDate date; //if null or blank set now
    @NotNull(message = "Поле medicineId не должно быть null")
    Long medicineId;
    @NotBlank(message = "Поле medicineBatchNumber не должно быть пустым")
    String medicineBatchNumber;
    Boolean isPeriodical;
    Integer periodDays;

    @JsonCreator
    public DewormingDto(LocalDate date, Long medicineId,
                        String medicineBatchNumber, Boolean isPeriodical, Integer periodDays) {
        this.date = date;
        this.medicineId = medicineId;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
    }
}
