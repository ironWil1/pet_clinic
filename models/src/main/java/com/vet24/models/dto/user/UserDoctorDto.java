package com.vet24.models.dto.user;

import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.enums.DoctorSpecEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class UserDoctorDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String avatar;
    private List<DiagnosisDto> diagnoses;
    private List<DoctorSpecEnum> specs;
}
