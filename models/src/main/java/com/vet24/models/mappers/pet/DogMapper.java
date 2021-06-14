package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Dog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DogMapper extends DtoMapper<Dog, DogDto>, EntityMapper<DogDto, Dog> {

}
