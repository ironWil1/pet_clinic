package com.vet24.models.dto.pet;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetFoundClientDto {

    private Long id;
    private String latitude;
    private String longitude;
    private String message;
    private LocalDateTime foundDate;
}
