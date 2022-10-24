package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetRequestPostDto;
import com.vet24.models.dto.pet.PetRequestPutDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class PetMapper implements DtoMapper<Pet, PetResponseDto>, EntityMapper<PetResponseDto, Pet> {
    @Mapping(source = "size", target ="petSize")
    public abstract Pet toEntity(PetRequestPostDto postDto);

    @Mapping(source = "size", target = "petSize")
    public abstract void updateEntity(PetRequestPutDto putDto, @MappingTarget Pet entity);
}
