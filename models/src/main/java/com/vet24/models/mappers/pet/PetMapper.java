package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(source = "petType", target = "type")
    PetDto petToPetDto(Pet pet);

    default <T extends Pet> T AbstractNewPetDtoToPet(AbstractNewPetDto petDto) {
        T pet = null;
        String petType = petDto.getPetType().name();
        switch (petType) {
            case "DOG":
                pet = (T) DogDtoToDog((DogDto) petDto);
                break;
            case "CAT":
                pet = (T) CatDtoToCat((CatDto) petDto);
                break;
            default:
                break;
        }
        return pet;
    }

    Dog DogDtoToDog(DogDto dogDto);

    DogDto DogToDogDto(Dog dog);

    Cat CatDtoToCat(CatDto catDto);

    CatDto CatToCatDto(Cat cat);
}
