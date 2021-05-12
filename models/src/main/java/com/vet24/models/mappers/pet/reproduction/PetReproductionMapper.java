package com.vet24.models.mappers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.PetReproductionDto;
import com.vet24.models.pet.reproduction.Reproduction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetReproductionMapper {

    PetReproductionDto reproductionToReproductionDto(Reproduction reproduction);

    Reproduction reproductionDtoToReproduction(PetReproductionDto reproductionDto);
}
