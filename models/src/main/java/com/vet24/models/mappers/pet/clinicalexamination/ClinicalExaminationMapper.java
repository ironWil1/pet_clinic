package com.vet24.models.mappers.pet.clinicalexamination;

import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicalExaminationMapper {

    ClinicalExaminationDto clinicalExaminationToClinicalExaminationDto(ClinicalExamination clinicalExamination);

    ClinicalExamination clinicalExaminationDtoToClinicalExamination(ClinicalExaminationDto clinicalExaminationDto);

    List<ClinicalExaminationDto> ClinicalExaminationListToClinicalExaminationDto(List<ClinicalExamination> clinicalExaminationList);
}
