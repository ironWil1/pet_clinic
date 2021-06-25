package com.vet24.models.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class PetContactDto {
    @NotBlank(message = "Введите имя владельца")
    private String ownerName;

    @NotBlank
    private String address;

    @NotNull
    private Long phone;
}

