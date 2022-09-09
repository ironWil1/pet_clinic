package com.vet24.models.mappers.pet;


import com.vet24.models.dto.pet.PetRequestDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Pet;

public interface AbstractPetMapper {
    PetType getPetType();
    Pet petRequestDtoToPet(PetRequestDto petDto);
    Pet abstractPetDtoToPet(PetResponseDto petResponseDto);
}
