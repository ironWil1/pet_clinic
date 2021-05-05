package com.vet24.models.mappers;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MedicineMapper {

    MedicineDto medicineToMedicineDto(Medicine medicine);

    Medicine medicineDtoToMedicine(MedicineDto medicineDto);
}
