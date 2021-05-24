package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public abstract class PetMapper {

    @Autowired
    private DogMapper dogMapper;

    @Autowired
    private CatMapper catMapper;

    @Mapping(source = "petType", target = "type")
    public abstract PetDto petToPetDto(Pet pet);

    public PetDto petToPetDtoWithWeekNotificationCount(Pet pet) {
        PetDto dto = this.petToPetDto(pet);
        int notificationCount = (int) pet.getNotifications().stream()
                .filter(item -> item.getStartDate().getTime() <
                        Timestamp.valueOf(LocalDateTime.of(LocalDate.now().plusDays(7L), LocalTime.MIDNIGHT)).getTime())
                .count();
        dto.setNotificationCount(notificationCount);

        return dto;
    }

    public Pet abstractNewPetDtoToPet(AbstractNewPetDto petDto) {
        Pet pet = null;
        String petType = petDto.getPetType().name();
        switch (petType) {
            case "DOG":
                pet = dogMapper.dogDtoToDog((DogDto) petDto);
                break;
            case "CAT":
                pet = catMapper.catDtoToCat((CatDto) petDto);
                break;
            default:
                break;
        }
        return pet;
    }
}
