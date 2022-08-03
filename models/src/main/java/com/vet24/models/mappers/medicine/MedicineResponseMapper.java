package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.MedicineResponseDto;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.mappers.user.UserInfoMapper;
import com.vet24.models.medicine.Medicine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserInfoMapper.class)
public interface MedicineResponseMapper extends EntityMapper<MedicineResponseDto, Medicine> {

}
