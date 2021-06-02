package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExternalParasiteMapper extends AbstractProcedureMapper {

    @Mapping(source = "medicine.id", target = "medicineId")
    ExternalParasiteDto externalParasiteToExternalParasiteDto(ExternalParasiteProcedure externalParasiteProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    ExternalParasiteProcedure externalParasiteDtoToExternalParasite(ExternalParasiteDto externalParasiteDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    ExternalParasiteProcedure procedureDtoToExternalParasite(ProcedureDto procedureDto);

    @Override
    default ProcedureType getProcedureType() {
        return ProcedureType.EXTERNAL_PARASITE;
    }

    @Override
    default Procedure AbstractProcedureDtoToProcedure(AbstractNewProcedureDto abstractNewProcedureDto) {
        return externalParasiteDtoToExternalParasite((ExternalParasiteDto) abstractNewProcedureDto);
    }

    @Override
    default Procedure ProcedureDtoToProcedure(ProcedureDto procedureDto) {
        return procedureDtoToExternalParasite(procedureDto);
    }
}
