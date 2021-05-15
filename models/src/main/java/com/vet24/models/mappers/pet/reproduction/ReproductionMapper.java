package com.vet24.models.mappers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.pet.reproduction.Reproduction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReproductionMapper {

    @Mapping(source = "id", target = "id")
    ReproductionDto reproductionToReproductionDto(Reproduction reproduction);

    @Mapping(source = "id", target = "id")
    Reproduction reproductionDtoToReproduction(ReproductionDto reproductionDto);
}
