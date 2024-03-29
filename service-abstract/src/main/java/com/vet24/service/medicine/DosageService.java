package com.vet24.service.medicine;

import com.vet24.models.medicine.Dosage;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface DosageService extends ReadWriteService<Long, Dosage> {

    List<Dosage> getByMedicineId(Long medicineId);

    Boolean isDosageExists(Long medicineId, String dosageType, Integer dosageSize);
}
