package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EchinococcusMapper extends AbstractProcedureMapper {

    @Mapping(source = "medicine.id", target = "medicineId")
    EchinococcusDto echinococcusToEchinococcusDto(EchinococcusProcedure echinococcusProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    EchinococcusProcedure echinococcusDtoToEchinococcus(EchinococcusDto echinococcusDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    EchinococcusProcedure procedureDtoToEchinococcus(ProcedureDto procedureDto);

    @Override
    default ProcedureType getProcedureType() {
        return ProcedureType.ECHINOCOCCUS;
    }

    @Override
    default Procedure AbstractProcedureDtoToProcedure(AbstractNewProcedureDto abstractNewProcedureDto) {
        return echinococcusDtoToEchinococcus((EchinococcusDto) abstractNewProcedureDto);
    }

    @Override
    default Procedure ProcedureDtoToProcedure(ProcedureDto procedureDto) {
        return procedureDtoToEchinococcus(procedureDto);
    }
}
