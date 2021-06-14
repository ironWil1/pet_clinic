package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Cat;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PetMapper implements DtoMapper<Pet, PetDto>, EntityMapper<PetDto, Pet> {

    @Autowired
    CatMapper catMapper;
    @Autowired
    DogMapper dogMapper;

    @Override
    public Pet toEntity (PetDto dto){
        if (dto instanceof CatDto) {
            return catMapper.toEntity((CatDto) dto);
        }
        if (dto instanceof DogDto) {
            return dogMapper.toEntity((DogDto) dto);
        }
        throw new NoSuchAbstractEntityDtoException("Can't find mapper for PetDto: " + dto);
    }

    @Override
    public PetDto toDto (Pet entity){
        if (entity instanceof Cat) {
            return catMapper.toDto((Cat) entity);
        }
        if (entity instanceof Dog) {
            return dogMapper.toDto((Dog) entity);
        }
        throw new NoSuchAbstractEntityDtoException("Can't find mapper for Pet: " + entity);
    }

}
