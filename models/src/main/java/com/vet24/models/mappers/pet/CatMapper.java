package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.pet.Cat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatMapper {

    Cat catDtoToCat(CatDto catDto);

    CatDto catToCatDto(Cat cat);
}
