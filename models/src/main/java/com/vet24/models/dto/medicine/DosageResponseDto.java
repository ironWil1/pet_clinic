package com.vet24.models.dto.medicine;

import com.vet24.models.enums.DosageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DosageResponseDto {
    Long id;
    Integer dosageSize;
    DosageType dosageType;
}
