package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.pet.procedure.Procedure;

public interface AbstractProcedureMapper {
    ProcedureType getType();
    Procedure transformAbstractProcedureDto(AbstractNewProcedureDto abstractNewProcedureDto);
    Procedure transformProcedureDto(ProcedureDto procedureDto);
}
