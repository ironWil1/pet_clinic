package com.vet24.models.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "petType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DogDto.class, name = "DOG"),
        @JsonSubTypes.Type(value = CatDto.class, name = "CAT")
})
public abstract class AbstractNewPetDto {

    private String name;
    private PetType petType;
    private LocalDate birthDay;
    private Gender gender; //male, female
    private String breed;
    private String color;
    private PetSize size; //small, medium, big
    private Double weight; // кг, округляем до десятых - 10,1, 12,5
    private String description;

    @JsonCreator
    public AbstractNewPetDto(@JsonProperty String name, @JsonProperty PetType petType, @JsonProperty LocalDate birthDay,
                             @JsonProperty Gender gender, @JsonProperty String breed, @JsonProperty String color,
                             @JsonProperty PetSize size, @JsonProperty Double weight, @JsonProperty String description) {
        this.name = name;
        this.petType = petType;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.color = color;
        this.size = size;
        this.weight = weight;
        this.description = description;
    }
    public AbstractNewPetDto() {

    }
}
