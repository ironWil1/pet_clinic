package com.vet24.models.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class MedicineRequestDto {
    @NotBlank(message = "Поле manufactureName не должно быть пустым")
    String manufactureName;

    @NotBlank(message = "Поле name не должно быть пустым")
    String name;

    @NotBlank(message = "Поле iconUrl не должно быть пустым")
    String iconUrl;

    @NotBlank(message = "Поле description не должно быть пустым")
    String description;
}
