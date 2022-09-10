package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.dto.pet.PetRequestDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class PetMapper implements DtoMapper<Pet, PetResponseDto>, EntityMapper<PetResponseDto, Pet> {
    private Map<PetType, AbstractPetMapper> mapperMap;

    @Autowired
    private List<AbstractPetMapper> mapperList;

    @PostConstruct
    private void init() {
        this.setMapperMap(mapperList);
    }

    private void setMapperMap(List<AbstractPetMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractPetMapper::getPetType, Function.identity()));
    }

    @Override
    public Pet toEntity(PetResponseDto petResponseDto) {
        if (mapperMap.containsKey(petResponseDto.getPetType())) {
            return mapperMap.get(petResponseDto.getPetType()).abstractPetDtoToPet(petResponseDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + petResponseDto);
        }
    }

    public Pet toEntity(PetRequestDto petRequestDto) {
        if (mapperMap.containsKey(petRequestDto.getPetType())) {
            return mapperMap.get(petRequestDto.getPetType()).petRequestDtoToPet(petRequestDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + petRequestDto);
        }
    }

    public abstract void updateEntity(PetRequestDto dto, @MappingTarget Pet entity);
}
