package com.vet24.service.pet.clinicalexamination;

import com.vet24.dao.pet.clinicalexamination.ClinicalExaminationDao;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.service.ReadWriteServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicalExaminationServiceImpl extends ReadWriteServiceImpl<Long, ClinicalExamination>
        implements ClinicalExaminationService {

    private final ClinicalExaminationDao clinicalExaminationDao;

    public ClinicalExaminationServiceImpl(ClinicalExaminationDao clinicalExaminationDao) {
        super(clinicalExaminationDao);
        this.clinicalExaminationDao = clinicalExaminationDao;
    }

    @Override
    public boolean isExistByPetId(Long petId) {
        return clinicalExaminationDao.isExistByPetId(petId);
    }

    @Override
    public List<ClinicalExamination> getByPetId(Long petId) {
        return clinicalExaminationDao.getByPetId(petId);
    }

    @Override
    public ClinicalExamination getById(Long examId) {
        return clinicalExaminationDao.getById(examId);
    }
}