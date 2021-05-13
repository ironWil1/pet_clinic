package com.vet24.models.dto.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CatDto extends AbstractNewPetDto {

    private String avatar;
    private Integer notificationCount;
    private PetType petType;

    public CatDto() {
        super();
    }

    @JsonCreator
    public CatDto(String name, LocalDate birthDay,
                  Gender gender, String breed, String color,
                  PetSize size, Double weight, String description,
                  String avatar, Integer notificationCount) {
        super(name, birthDay, gender, breed, color, size, weight, description);
        this.avatar = avatar;
        this.notificationCount = notificationCount;
        this.petType = PetType.CAT;
    }
}
