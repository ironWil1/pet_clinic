package com.vet24.models.mappers.user;



import com.vet24.models.dto.user.DoctorDto;
import com.vet24.models.mappers.medicine.DiagnosisMapper;
import com.vet24.models.user.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DiagnosisMapper.class})
public interface DoctorMapper {


    DoctorDto doctorToDoctorDto(Doctor doctor);
}
