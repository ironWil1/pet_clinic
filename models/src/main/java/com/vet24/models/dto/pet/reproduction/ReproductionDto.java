package com.vet24.models.dto.pet.reproduction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReproductionDto {
    @JsonView(View.Get.class)
    Long id;
    @JsonView({View.Put.class, View.Get.class})
    LocalDate estrusStart;
    @JsonView({View.Put.class, View.Get.class})
    LocalDate mating;
    @JsonView({View.Put.class, View.Get.class})
    LocalDate dueDate;
    @JsonView({View.Put.class, View.Get.class})
    Integer childCount;
}
