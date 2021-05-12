package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.pet.Dog;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface DogMapper {

    Dog dogDtoToDog(DogDto dogDto);

    DogDto dogToDogDto(Dog dog);
}
