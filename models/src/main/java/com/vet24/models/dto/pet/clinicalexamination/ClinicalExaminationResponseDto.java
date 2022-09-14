package com.vet24.models.dto.pet.clinicalexamination;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.View;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalExaminationResponseDto {
    @JsonView(View.Get.class)
    private Long id;
    @JsonView({View.Put.class, View.Get.class})
    private Long petId;
    @JsonView({View.Put.class, View.Get.class})
    private Double weight;
    @JsonView({View.Put.class, View.Get.class})
    private Boolean isCanMove;
    @JsonView({View.Put.class, View.Get.class})
    private String text;
}
