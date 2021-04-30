package com.vet24.models.dto.medicine;

import lombok.Data;

@Data
public class MedicineDto {
    Long id;
    String manufactureName;
    String name;
    String icon;
    String description;
}
