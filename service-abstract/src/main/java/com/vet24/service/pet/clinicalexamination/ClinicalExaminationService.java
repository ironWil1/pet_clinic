package com.vet24.service.pet.clinicalexamination;

import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.service.ReadWriteService;

import java.util.List;


public interface ClinicalExaminationService extends ReadWriteService<Long, ClinicalExamination> {

    List<ClinicalExamination> getByPetId(Long petId);

    boolean isExistByIdAndDoctorId(Long examId, Long doctorId);

    boolean isExistByPetIdAndDoctorId(Long petId, Long doctorId);

    ClinicalExamination getById(Long examId);
}
