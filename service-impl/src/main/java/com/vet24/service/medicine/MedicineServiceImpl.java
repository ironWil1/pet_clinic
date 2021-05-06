package com.vet24.service.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.medicine.MedicineDao;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.ReadWriteServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicineServiceImpl extends ReadWriteServiceImpl<Long, Medicine> implements MedicineService {

    private final MedicineDao medicineDao;

    public MedicineServiceImpl(ReadWriteDaoImpl<Long, Medicine> readWriteDao, MedicineDao medicineDao) {
        super(readWriteDao);
        this.medicineDao = medicineDao;
    }

    @Transactional
    @Override
    public List<Medicine> searchFull(String manufactureName, String name, String searchText) {
        if (searchText.equals("")) {
            return medicineDao.search(manufactureName, name);
        } else {
            return  medicineDao.searchFull(manufactureName, name, searchText);
        }
    }
}
