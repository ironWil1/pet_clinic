package com.vet24.service.medicine;

import com.vet24.dao.medicine.DosageDao;
import com.vet24.models.medicine.Dosage;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DosageServiceImpl extends ReadWriteServiceImpl<Long, Dosage>
        implements DosageService{

    private final DosageDao dosageDao;

    public DosageServiceImpl(DosageDao dosageDao) {
        super(dosageDao);
        this.dosageDao = dosageDao;
    }

    @Override
    public List<Dosage> getByMedicineId(Long medicineId) {
        return dosageDao.getByMedicineId(medicineId);
    }

    @Override
    public Boolean isDosageExists(Long medicineId, String dosageType, Integer dosageSize) {
        return dosageDao.isMedicineIdAndDosageTypeAndDosageSizeCombinationExist(medicineId, dosageType, dosageSize);
    }

}
