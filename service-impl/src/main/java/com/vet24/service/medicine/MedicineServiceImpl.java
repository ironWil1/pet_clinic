package com.vet24.service.medicine;

import com.vet24.dao.medicine.MedicineDao;
import com.vet24.models.medicine.Medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    MedicineDao medicineDao;


    @Transactional
    @Override
    public Medicine getMedicineById(Long id) {
        return medicineDao.getMedicineById(id);
    }

    @Transactional
    @Override
    public List<Medicine> getAllMedicine() {
        return medicineDao.getAllMedicine();
    }

    @Transactional
    @Override
    public void addMedicine(Medicine medicine) {
        medicineDao.addMedicine(medicine);
    }

    @Transactional
    @Override
    public void editMedicine(Medicine medicine) {
        medicineDao.editMedicine(medicine);
    }

    @Transactional
    @Override
    public void deleteMedicine(Long id) {
        medicineDao.deleteMedicine(id);
    }

    @Override
    public List<Medicine> searchFull(String manufactureName, String name, String searchtext) {
        return  medicineDao.searchFull(manufactureName, name, searchtext);
    }

    @Override
    public List<Medicine> search(String manufactureName, String name) {
        return  medicineDao.search(manufactureName, name);
    }


}
