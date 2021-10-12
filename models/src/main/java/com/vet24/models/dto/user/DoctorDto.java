package com.vet24.models.dto.user;

import com.vet24.models.dto.medicine.DiagnosisDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DoctorDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
}