package com.vet24.models.mappers.pet.clinicalexamination;

import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicalExaminationMapper {

    @Mapping(source = "id", target = "id")
    ClinicalExaminationDto clinicalExaminationToClinicalExaminationDto(ClinicalExamination clinicalExamination);

    @Mapping(source = "id", target = "id")
    ClinicalExamination clinicalExaminationDtoToClinicalExamination(ClinicalExaminationDto clinicalExaminationDto);
}
