package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.medicine.Dosage;

import java.util.List;

public interface DosageDao extends ReadWriteDao<Long, Dosage> {

    List<Dosage> getByMedicineId(Long medicineId);

    Boolean isDosageTypeAndDosageSizeCombinationExist(String dosageType, Integer dosageSize);

}
