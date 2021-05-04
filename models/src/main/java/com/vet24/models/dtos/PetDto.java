package com.vet24.models.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vet24.models.enums.PetType;
import lombok.Data;

import java.time.LocalDate;

@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = DogDto.class, name = "DOG"),
//        @JsonSubTypes.Type(value = CatDto.class, name = "CAT")
//})
public class PetDto {

    private Long id;
    private String name;
    private String avatar;
    private LocalDate birthDay;
    private Integer notificationCount;
    private PetType type; //dog, cat (в будущем добавим еще видов питомцев)
}
