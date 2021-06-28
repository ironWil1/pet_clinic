package com.vet24.models.dto.medicine;


import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class MedicineDto {
    @Null(groups = {OnCreate.class}, message = "поле id должно быть null")
    @NotNull(groups = {OnUpdate.class}, message = "поле id не должно быть null")
    Long id;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "поле manufactureName не должно быть пустым")
    String manufactureName;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "поле name не должно быть пустым")
    String name;
    String icon;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "поле description не должно быть пустым")
    String description;

}
