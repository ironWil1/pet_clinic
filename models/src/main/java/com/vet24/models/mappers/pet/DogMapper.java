package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetRequestDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DogMapper extends AbstractPetMapper, DtoMapper<Dog, DogDto>, EntityMapper<DogDto, Dog> {

    @Override
    default PetType getPetType() {
        return PetType.DOG;
    }

    @Override
    default Pet petRequestDtoToPet(PetRequestDto petDto) {
        return toEntity(petDto);
    }

    Dog toEntity(PetRequestDto petResponseDto);

    @Override
    default Pet abstractPetDtoToPet(PetResponseDto petResponseDto) {
        return toEntity(petResponseDto);
    }

    Dog toEntity(PetResponseDto petResponseDto);

}
