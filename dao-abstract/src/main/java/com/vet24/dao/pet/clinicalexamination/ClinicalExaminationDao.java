package com.vet24.dao.pet.clinicalexamination;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;

import java.util.List;

public interface ClinicalExaminationDao extends ReadWriteDao<Long, ClinicalExamination> {

    List<ClinicalExamination> getByPetId(Long petId);

    boolean isExistByIdAndDoctorId(Long examId, Long doctorId);

    boolean isExistByPetIdAndDoctorId(Long petId, Long doctorId);
}
