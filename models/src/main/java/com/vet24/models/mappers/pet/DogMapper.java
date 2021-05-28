package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DogMapper extends AbstractPetMapper{

    Dog dogDtoToDog(DogDto dogDto);

    DogDto dogToDogDto(Dog dog);

    @Override
    default PetType getPetType() {
        return PetType.DOG;
    }

    @Override
    default Pet transferAbstractPetDto(AbstractNewPetDto petDto) {
        return dogDtoToDog((DogDto) petDto);
    }
}
