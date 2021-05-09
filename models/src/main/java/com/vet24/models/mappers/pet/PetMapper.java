package com.vet24.models.mappers.pet;
import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    @Mapping(source = "petType", target = "type")
    PetDto petToPetDto(Pet pet);

    default <T extends Pet> T AbstractNewPetDtoToPet(AbstractNewPetDto petDto) {
        if (Objects.equals(petDto.getPetType().getType(), "DOG")) {
            return (T) DogDtoToDog((DogDto) petDto);
        }
        return null;
    }

    DogDto PetDtoToDogDto(PetDto petDto);

    Dog DogDtoToDog(DogDto dogDto);

    DogDto DogToDogDto(Dog dog);
}
