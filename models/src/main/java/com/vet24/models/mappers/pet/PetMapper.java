package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PetMapper {

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

    @Mapping(target = "type", source = "petType")
    @Mapping(target = "notificationCount", source = "pet")
    public abstract PetDto petToPetDto(Pet pet);

    protected int petToNotificationCountInt(Pet pet) {
        return (int) pet.getNotifications().stream()
                .filter(item -> item.getStartDate().getTime() <
                        Timestamp.valueOf(LocalDateTime.of(LocalDate.now().plusDays(7L), LocalTime.MIDNIGHT)).getTime())
                .count();
    }

    public Pet abstractNewPetDtoToPet(AbstractNewPetDto petDto) {
        if (mapperMap.containsKey(petDto.getPetType())) {
            return mapperMap.get(petDto.getPetType()).abstractPetDtoToPet(petDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + petDto);
        }
    }
}
