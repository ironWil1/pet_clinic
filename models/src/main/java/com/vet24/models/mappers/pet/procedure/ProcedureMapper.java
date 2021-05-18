package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.*;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProcedureMapper {

    @Autowired
    private VaccinationMapper vaccinationMapper;

    @Autowired
    private ExternalParasiteMapper externalParasiteMapper;

    @Autowired
    private EchinococcusMapper echinococcusMapper;

    @Mapping(source = "medicine.id", target = "medicineId")
    public abstract ProcedureDto procedureToProcedureDto(Procedure procedure);

    public Procedure procedureDtoToProcedure(ProcedureDto procedureDto) {
        Procedure procedure = null;
        String procedureType = procedureDto.getType().name();
        switch (procedureType) {
            case "VACCINATION":
                procedure = vaccinationMapper.procedureDtoToVaccination(procedureDto);
                break;
            case "EXTERNAL_PARASITE":
                procedure = externalParasiteMapper.procedureDtoToExternalParasite(procedureDto);
                break;
            case "ECHINOCOCCUS":
                procedure = echinococcusMapper.procedureDtoToEchinococcus(procedureDto);
                break;
            default:
                break;
        }

        return procedure;
    }

    public Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto procedureDto) {
        Procedure procedure = null;
        String procedureType = procedureDto.getType().name();
        switch (procedureType) {
            case "VACCINATION":
                procedure = vaccinationMapper.vaccinationDtoToVaccination((VaccinationDto) procedureDto);
                break;
            case "EXTERNAL_PARASITE":
                procedure = externalParasiteMapper.externalParasiteDtoToExternalParasite((ExternalParasiteDto) procedureDto);
                break;
            case "ECHINOCOCCUS":
                procedure = echinococcusMapper.echinococcusDtoToEchinococcus((EchinococcusDto) procedureDto);
                break;
            default:
                break;
        }

        return procedure;
    }
}
