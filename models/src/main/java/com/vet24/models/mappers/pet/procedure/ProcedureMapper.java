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

    @Autowired
    List<AbstractProcedureMapper> mapperList;

    @PostConstruct
    public void init() {
        this.setMapperMap(mapperList);
    }

    public void setMapperMap(List<AbstractProcedureMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractProcedureMapper::getProcedureType, Function.identity()));
    }

    @Mapping(source = "medicine.id", target = "medicineId")
    public abstract ProcedureDto procedureToProcedureDto(Procedure procedure);

    public Procedure procedureDtoToProcedure(ProcedureDto procedureDto) {
        if (mapperMap.containsKey(procedureDto.getType())) {
            return mapperMap.get(procedureDto.getType()).ProcedureDtoToProcedure(procedureDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find mapper for Procedure: " + procedureDto);
        }
    }

    public Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto procedureDto) {
        if (mapperMap.containsKey(procedureDto.getType())) {
            return mapperMap.get(procedureDto.getType()).AbstractProcedureDtoToProcedure(procedureDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find mapper for AbstractNewProcedureDto: " + procedureDto);
        }
    }
}
