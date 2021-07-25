package com.vet24.models.dto.medicine;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDto {
    private Long id;
    private Long petId;
    private Long doctorId;
    private String description;

}
