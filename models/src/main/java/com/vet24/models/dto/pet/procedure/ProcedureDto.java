package com.vet24.models.dto.pet.procedure;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureDto {

    @JsonView(View.Get.class)
    Long id;

    @JsonView({View.Put.class, View.Get.class})
    LocalDate date; //if null or blank set now

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
}
