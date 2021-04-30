package com.vet24.dao.medicine;

import com.vet24.models.medicine.Medicine;
import com.vet24.models.user.User;

import java.util.List;

public interface MedicineDao {
    Medicine getMedicineById(Long id);
    List<Medicine> getAllMedicine();
    void addMedicine(Medicine medicine);
    void editMedicine(Medicine medicine);
    void deleteMedicine(Long id);
}
