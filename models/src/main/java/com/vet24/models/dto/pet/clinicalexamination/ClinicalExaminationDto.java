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
    @JsonView({View.Put.class, View.Get.class})
    Long petId;
    @JsonView({View.Put.class, View.Get.class})
    Double weight;
    @JsonView({View.Put.class, View.Get.class})
    Boolean isCanMove;
    @JsonView({View.Put.class, View.Get.class})
    String text;
}
