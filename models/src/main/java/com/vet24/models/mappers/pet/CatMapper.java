package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Cat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatMapper extends DtoMapper<Cat, CatDto>, EntityMapper<CatDto, Cat> {

}
