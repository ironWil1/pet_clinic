package com.vet24.models.mappers.medicine;

import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses={ProcedureMapper.class})
public interface TreatmentMapper {


}
