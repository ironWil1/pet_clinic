package com.vet24.models.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetContactDto {
    private String ownerName;
    private String address;
    private Long phone;
}

