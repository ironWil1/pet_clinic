package com.vet24.models.dtos;

import com.vet24.models.enums.PetType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetDto {

    private Long id;
    private String name;
    private String avatar;
    private LocalDate birthDay;
    private Integer notificationCount;
    private PetType type; //dog, cat (в будущем добавим еще видов питомцев)
}
