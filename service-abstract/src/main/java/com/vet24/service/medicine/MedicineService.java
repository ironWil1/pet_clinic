package com.vet24.service.medicine;

import com.vet24.models.medicine.Medicine;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface MedicineService extends ReadWriteService<Long, Medicine> {

    List<Medicine> searchFull(String manufactureName, String name, String searchText);
}
