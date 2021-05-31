package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.*;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProcedureMapper {

    Map<ProcedureType, AbstractProcedureMapper> mapperMap;

    @PostConstruct
    @Autowired
    public void setMapperMap(List<AbstractProcedureMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractProcedureMapper::getType, Function.identity()));
    }

    @Mapping(source = "medicine.id", target = "medicineId")
    public abstract ProcedureDto procedureToProcedureDto(Procedure procedure);

    public Procedure procedureDtoToProcedure(ProcedureDto procedureDto) {
        if (mapperMap.containsKey(procedureDto.getType())) {
            return mapperMap.get(procedureDto.getType()).transformProcedureDto(procedureDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find mapper for Procedure: " + procedureDto);
        }
    }

    public Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto procedureDto) {
        if (mapperMap.containsKey(procedureDto.getType())) {
            return mapperMap.get(procedureDto.getType()).transformAbstractProcedureDto(procedureDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find mapper for AbstractNewProcedureDto: " + procedureDto);
        }
    }
}
