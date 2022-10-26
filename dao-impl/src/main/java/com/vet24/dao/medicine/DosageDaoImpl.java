package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.Dosage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DosageDaoImpl extends ReadWriteDaoImpl<Long, Dosage> implements DosageDao {

    @Override
    public List<Dosage> getByMedicineId(Long medicineId) {
        return manager.createQuery("SELECT d FROM Dosage d WHERE d.medicine.id = :medicineId", Dosage.class)
                .setParameter("medicineId",medicineId)
                .getResultList();
    }

    @Override
    public Boolean isDosageTypeAndDosageSizeCombinationExist(String dosageType, Integer dosageSize) {
        return (Boolean) manager.createNativeQuery("SELECT EXISTS(SELECT dosage_type, dosage_size FROM dosage WHERE " +
                        "dosage_type = :dosageType AND dosage_size = :dosageSize)")
                .setParameter("dosageType", dosageType).setParameter("dosageSize", dosageSize).getSingleResult();
    }
}
