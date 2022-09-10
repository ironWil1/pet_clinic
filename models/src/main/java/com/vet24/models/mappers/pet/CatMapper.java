package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetRequestDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatMapper extends AbstractPetMapper {

    @Override
    default PetType getPetType() {
        return PetType.CAT;
    }

    @Override
    default Pet petRequestDtoToPet(PetRequestDto petRequestDto) {
        return toEntity(petRequestDto);
    }

    Cat toEntity(PetRequestDto petRequestDto);

    @Override
    default Pet abstractPetDtoToPet(PetResponseDto petResponseDto) {
        return toEntity(petResponseDto);
    }

    Cat toEntity(PetResponseDto petResponseDto);

}
