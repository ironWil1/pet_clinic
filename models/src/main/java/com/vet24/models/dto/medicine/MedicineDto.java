package com.vet24.models.dto.medicine;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
public class MedicineDto {
    @Null
    Long id;
    @NotBlank
    String manufactureName;
    @NotBlank
    String name;
    String icon;
    String description;

}
