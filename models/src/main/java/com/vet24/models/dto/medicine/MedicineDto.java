package com.vet24.models.dto.medicine;


import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class MedicineDto {
    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    Long id;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    String manufactureName;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    String name;
    String icon;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    String description;

}
