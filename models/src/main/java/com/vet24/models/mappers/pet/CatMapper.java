package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatMapper extends AbstractPetMapper{

    Cat catDtoToCat(CatDto catDto);

    CatDto catToCatDto(Cat cat);

    @Override
    default PetType getPetType() {
        return PetType.CAT;
    }

    @Override
    default Pet abstractPetDtoToPet(AbstractNewPetDto petDto) {
        return catDtoToCat((CatDto) petDto);
    }
}
