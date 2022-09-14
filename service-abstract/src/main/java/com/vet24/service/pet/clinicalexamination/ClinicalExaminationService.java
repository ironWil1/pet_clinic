package com.vet24.service.pet.clinicalexamination;

import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.service.ReadWriteService;

import java.util.List;


public interface ClinicalExaminationService extends ReadWriteService<Long, ClinicalExamination> {

    boolean isExistByPetId(Long petId);

    List<ClinicalExamination> getByPetId(Long petId);

    ClinicalExamination getClinicalExaminationWithPetById(Long examId);
}
