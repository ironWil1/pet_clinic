package com.vet24.models.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetContactDto {
    private String ownerName;
    private String address;
    private String phone;
}

