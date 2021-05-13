package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.pet.Cat;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CatMapper {

    Cat catDtoToCat(CatDto catDto);

    CatDto catToCatDto(Cat cat);
}
