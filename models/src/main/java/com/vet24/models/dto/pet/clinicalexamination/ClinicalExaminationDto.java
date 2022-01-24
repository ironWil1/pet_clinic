package com.vet24.models.dto.pet.clinicalexamination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalExaminationDto {
    @JsonView(View.Get.class)
    Long id;
    Long petId;
    Double weight;
    Boolean isCanMove;
    String text;
}
