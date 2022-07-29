package com.vet24.models.dto.pet.procedure;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.util.View;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class VaccinationDto {

    @JsonView(View.Get.class)
    Long id;
    @JsonView({View.Post.class, View.Put.class, View.Get.class})
    private LocalDate date; //if null or blank set now


    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Поле medicineId не должно быть null")
    @JsonView({View.Post.class, View.Put.class, View.Get.class})
    private Long medicineId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Поле medicineBatchNumber не должно быть пустым")
    @JsonView({View.Post.class, View.Put.class, View.Get.class})
    private String medicineBatchNumber;

    @JsonView({View.Post.class, View.Put.class, View.Get.class})
    private Boolean isPeriodical;

    @JsonView({View.Post.class, View.Put.class, View.Get.class})
    private Integer periodDays;

    public VaccinationDto(LocalDate date, Long medicineId, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays) {
        this.date = date;
        this.medicineId = medicineId;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
    }

}
