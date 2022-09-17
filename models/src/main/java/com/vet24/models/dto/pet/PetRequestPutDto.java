package com.vet24.models.dto.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestPutDto {
    private String name;

    private String avatar;

    @PastOrPresent(message = "birthDay должно быть меньше или равно текущей дате")
    private LocalDate birthDay;

    private String breed;

    private Gender gender;

    private String color;

    private PetSize size;

    @Positive(message = "weight должно быть больше 0")
    private Double weight;

    private String description;
}
