package com.vet24.service.medicine;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;

import org.springframework.stereotype.Service;

@Service
public class Mapping {

    public MedicineDto mapToMedicineDto(Medicine medicine) {
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setId(medicine.getId());
        medicineDto.setManufactureName(medicine.getManufactureName());
        medicineDto.setName(medicine.getName());
        medicineDto.setIcon(medicine.getIcon());
        medicineDto.setDescription(medicine.getDescription());
        return medicineDto;
    }

    public Medicine mapToMedicine(MedicineDto medicineDto) {
        Medicine medicine = new Medicine();
        medicine.setManufactureName(medicineDto.getManufactureName());
        medicine.setName(medicineDto.getName());
        medicine.setIcon(medicineDto.getIcon());
        medicine.setDescription(medicineDto.getDescription());
        return medicine;
    }
}
