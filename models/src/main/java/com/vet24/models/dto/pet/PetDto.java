package com.vet24.models.dto.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "petType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DogDto.class, name = "DOG"),
        @JsonSubTypes.Type(value = CatDto.class, name = "CAT")
})
public abstract class PetDto {

    private String name;
    private PetType petType;
    private LocalDate birthDay;
    private Gender gender; //male, female
    private String breed;
    private String color;
    private PetSize petSize; //small, medium, big
    private Double weight; // кг, округляем до десятых - 10,1, 12,5
    private String description;

    @JsonCreator
    protected PetDto(String name, PetType petType, LocalDate birthDay,
                                Gender gender, String breed, String color,
                                PetSize size, Double weight, String description) {
        this.name = name;
        this.petType = petType;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.color = color;
        this.petSize = size;
        this.weight = weight;
        this.description = description;
    }
}
