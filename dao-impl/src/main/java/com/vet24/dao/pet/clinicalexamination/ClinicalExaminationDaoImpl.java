package com.vet24.dao.pet.clinicalexamination;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClinicalExaminationDaoImpl extends ReadWriteDaoImpl<Long, ClinicalExamination>
        implements ClinicalExaminationDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<ClinicalExamination> getByPetId(Long petId) {
        return manager.createQuery("from ClinicalExamination ce where ce.pet.id = :petId")
                .setParameter("petId", petId)
                .getResultList();
    }

    @Override
    public boolean isExistByIdAndDoctorId(Long examId, Long doctorId) {
        return manager.createQuery("SELECT CASE WHEN (count(*)>0) then true else false end " +
                                           "from ClinicalExamination ce where ce.id = :examId " +
                                           "and ce.doctor.id = :doctorId",
                                   Boolean.class)
                .setParameter("examId", examId)
                .setParameter("doctorId", doctorId)
                .getSingleResult();
    }

    @Override
    public boolean isExistByPetIdAndDoctorId(Long petId, Long doctorId) {
        return manager.createQuery("SELECT CASE WHEN (count(*)>0) then true else false end " +
                                           "from ClinicalExamination ce where ce.pet.id = :petId " +
                                           "and ce.doctor.id = :doctorId",
                                   Boolean.class)
                .setParameter("petId", petId)
                .setParameter("doctorId", doctorId)
                .getSingleResult();
    }
}
