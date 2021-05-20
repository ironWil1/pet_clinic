package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.pet.PetFound;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetFoundMapper {

    PetFound petFoundDtoToPetFound(PetFoundDto petFoundDto);
    PetFoundDto petFoundToPetFoundDto(PetFound petFound);
}
