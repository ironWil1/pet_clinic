package com.vet24.dao.pet.clinicalexamination;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClinicalExaminationDaoImpl extends ReadWriteDaoImpl<Long, ClinicalExamination>
        implements ClinicalExaminationDao {

    @Override
    public boolean isExistByPetId(Long petId) {
        return manager.createQuery("SELECT CASE WHEN (count(*)>0) then true else false end" +
                                           " from ClinicalExamination ce where ce.pet.id = :petId", Boolean.class)
                .setParameter("petId", petId)
                .getSingleResult();
    }

    @Override
    public List<ClinicalExamination> getByPetId(Long petId) {
        return manager.createQuery("from ClinicalExamination ce where ce.pet.id = :petId")
                .setParameter("petId", petId)
                .getResultList();
    }

    @Override
    public ClinicalExamination getById(Long examId) {
        return manager.createQuery("from ClinicalExamination ce join fetch ce.pet where ce.id = :examId",
                                   ClinicalExamination.class)
                .setParameter("examId", examId)
                .getSingleResult();
    }
}
