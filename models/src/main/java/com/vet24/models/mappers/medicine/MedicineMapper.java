package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;

import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MedicineMapper {

    MedicineDto medicineToMedicineDto(Medicine medicine);

    Medicine medicineDtoToMedicine(MedicineDto medicineDto);

    List<MedicineDto> medicineListToMedicineDto(List<Medicine> medicineList);
}
