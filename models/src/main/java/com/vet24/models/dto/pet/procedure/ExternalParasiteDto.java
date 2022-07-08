package com.vet24.models.dto.pet.procedure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ExternalParasiteDto {

    @JsonView(View.Get.class)
    Long id;

    @JsonView({View.Put.class, View.Get.class})
    LocalDate date;

    @JsonView({View.Put.class, View.Get.class})
    ProcedureType type;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Поле medicineId не должно быть null")
    @JsonView({View.Put.class, View.Get.class})
    Long medicineId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Поле medicineBatchNumber не должно быть пустым")
    @JsonView({View.Put.class, View.Get.class})
    String medicineBatchNumber;

    @JsonView({View.Put.class, View.Get.class})
    Boolean isPeriodical;

    @JsonView({View.Put.class, View.Get.class})
    Integer periodDays;

    public ExternalParasiteDto(Long id, LocalDate date, Long medicineId,
                               String medicineBatchNumber, Boolean isPeriodical, Integer periodDays) {
        this.id = id;
        this.date = date;
        this.medicineId = medicineId;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
    }
}
