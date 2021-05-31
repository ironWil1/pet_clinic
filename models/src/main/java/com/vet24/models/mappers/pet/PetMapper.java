package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PetMapper {

    Map<PetType, AbstractPetMapper> mapperMap;

    @PostConstruct
    @Autowired
    public void setMapperMap(List<AbstractPetMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractPetMapper::getPetType, Function.identity()));
    }

    @Mapping(source = "petType", target = "type")
    public abstract PetDto petToPetDto(Pet pet);

    public Pet abstractNewPetDtoToPet(AbstractNewPetDto petDto) {
        if (mapperMap.containsKey(petDto.getPetType())) {
            return mapperMap.get(petDto.getPetType()).transformAbstractPetDto(petDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + petDto);
        }
    }
}
