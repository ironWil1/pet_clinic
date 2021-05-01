package com.vet24.service.medicine;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.vet24.dao.medicine.MedicineDao;
import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    MedicineDao medicineDao;

    @Autowired
    Mapping mapping;

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
    public List<Medicine> search(String manufactureName, String name, String searchtext) {
        return  medicineDao.search(manufactureName, name, searchtext);
    }

    public List<MedicineDto> findAll() {
        return medicineDao.getAllMedicine()
                .stream().map(mapping::mapToMedicineDto)
                .collect(Collectors.toList());
    }

    public MedicineDto findById(Long id) {
        return mapping.mapToMedicineDto(
                medicineDao.getMedicineById(id));
    }
}
