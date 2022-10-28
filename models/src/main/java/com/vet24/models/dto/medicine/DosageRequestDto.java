package com.vet24.models.dto.medicine;

import com.vet24.models.enums.DosageType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DosageRequestDto {

    @NotBlank(message = "Поле dosageSize не должно быть пустым")
    Integer dosageSize;

    @NotBlank(message = "Поле dosageType не должно быть пустым")
    DosageType dosageType;
}
