package com.vet24.dao.pet.clinicalexamination;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicalExaminationDaoImpl extends ReadWriteDaoImpl<Long, ClinicalExamination> implements ClinicalExaminationDao {
}
