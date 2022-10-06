package com.vet24.models.dto.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestPostDto {
    @NotBlank(message = "name не должно быть пустым")
    private String name;

    private String avatar;

    @NotNull(message = "birthDay не должно быть пустым")
    @PastOrPresent(message = "birthDay должно быть меньше или равно текущей дате")
    private LocalDate birthDay;

    @NotNull(message = "petType не должно быть пустым")
    private PetType petType;

    @NotBlank(message = "breed не должно быть пустым")
    private String breed;

    @NotNull(message = "gender не должно быть пустым")
    private Gender gender;

    @NotBlank(message = "color не должно быть пустым")
    private String color;

    private PetSize size;

    @Positive(message = "weight должно быть больше 0")
    private Double weight;

    private String description;
}
