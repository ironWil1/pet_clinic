package com.vet24.models.mappers;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    MedicineDto medicineToMedicineDto(Medicine medicine);

    Medicine medicineDtoToMedicine(MedicineDto medicineDto);
}
