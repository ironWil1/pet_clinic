package com.vet24.models.dto.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DogDto extends AbstractNewPetDto {

    private String avatar;
    private Integer notificationCount;

    public DogDto() {
        super();
    }

    @JsonCreator
    public DogDto(@JsonProperty String name, @JsonProperty PetType petType, @JsonProperty LocalDate birthDay,
                  @JsonProperty Gender gender, @JsonProperty String breed, @JsonProperty String color,
                  @JsonProperty PetSize size, @JsonProperty Double weight, @JsonProperty String description,
                  @JsonProperty String avatar, @JsonProperty Integer notificationCount) {
        super(name, PetType.DOG, birthDay, gender, breed, color, size, weight, description);
        this.avatar = avatar;
        this.notificationCount = notificationCount;
    }
}
