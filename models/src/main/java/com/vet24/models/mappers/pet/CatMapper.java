package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetRequestPostDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatMapper extends AbstractPetMapper {

    @Override
    default PetType getPetType() {
        return PetType.CAT;
    }

    @Override
    default Pet petRequestPostDtoToPet(PetRequestPostDto petRequestDto) {
        return toEntity(petRequestDto);
    }

    @Mapping(source = "size", target = "petSize")
    Cat toEntity(PetRequestPostDto petRequestDto);

    @Override
    default Pet abstractPetDtoToPet(PetResponseDto petResponseDto) {
        return toEntity(petResponseDto);
    }

    Cat toEntity(PetResponseDto petResponseDto);

}
