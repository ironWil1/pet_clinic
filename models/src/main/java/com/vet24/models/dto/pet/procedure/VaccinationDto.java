package com.vet24.models.dto.pet.procedure;


import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.util.View;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode

public class VaccinationDto {


    @JsonView({View.Put.class, View.Get.class})
    private LocalDate date; //if null or blank set now


    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Поле medicineId не должно быть null")
    @JsonView({View.Put.class, View.Get.class})
    private Long medicineId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Поле medicineBatchNumber не должно быть пустым")
    @JsonView({View.Put.class, View.Get.class})
    private String medicineBatchNumber;
    @JsonView({View.Put.class, View.Get.class})
    private ProcedureType type;

    @JsonView({View.Put.class, View.Get.class})
    private Boolean isPeriodical;

    @JsonView({View.Put.class, View.Get.class})
    private Integer periodDays;

    public VaccinationDto(LocalDate date, Long medicineId, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays) {
        this.date = date;
        this.medicineId = medicineId;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.type = ProcedureType.VACCINATION;
    }

    public void setType(ProcedureType type) {
        this.type = ProcedureType.VACCINATION;
    }
    public ProcedureType getType() {
        return ProcedureType.VACCINATION;
    }
}
