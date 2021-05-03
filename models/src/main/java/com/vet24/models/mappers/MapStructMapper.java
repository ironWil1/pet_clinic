package com.vet24.models.mappers;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

    @Mapping(source = "name", target = "name")
    MedicineDto medicineToMedicineDto(Medicine medicine);

    @Mapping(target = "id", ignore = true)
    Medicine medicineDtoToMedicine(MedicineDto medicineDto);
}
