package com.vet24.models.dto.medicine;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {
    @JsonIgnore
    Long id;
    @NotBlank( message = "поле manufactureName не должно быть пустым")
    String manufactureName;
    @NotBlank( message = "поле name не должно быть пустым")
    String name;
    String icon;
    @NotBlank( message = "поле description не должно быть пустым")
    String description;
}
