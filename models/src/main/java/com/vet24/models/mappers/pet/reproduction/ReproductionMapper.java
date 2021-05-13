package com.vet24.models.mappers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.pet.reproduction.Reproduction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReproductionMapper {

    ReproductionDto reproductionToReproductionDto(Reproduction reproduction);

    Reproduction reproductionDtoToReproduction(ReproductionDto reproductionDto);
}
