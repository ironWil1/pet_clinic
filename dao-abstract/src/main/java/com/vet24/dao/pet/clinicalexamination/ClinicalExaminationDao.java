package com.vet24.dao.pet.clinicalexamination;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;

import java.util.List;

public interface ClinicalExaminationDao extends ReadWriteDao<Long, ClinicalExamination> {

    boolean isExistByPetId(Long petId);

    List<ClinicalExamination> getByPetId(Long petId);

    ClinicalExamination getClinicalExaminationWithPetById(Long examId);
}
