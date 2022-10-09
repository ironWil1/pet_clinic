package com.vet24.models.mappers.pet;


import com.vet24.models.dto.pet.PetRequestPostDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Pet;

public interface AbstractPetMapper {
    PetType getPetType();
    Pet petRequestPostDtoToPet(PetRequestPostDto petDto);
    Pet abstractPetDtoToPet(PetResponseDto petResponseDto);
}
