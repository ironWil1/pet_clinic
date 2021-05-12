package com.vet24.models.mappers;

import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.pet.PetContact;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetContactMapper {

    PetContact petContactDtoToPetContact(PetContactDto petContactDto);

    PetContactDto petContactToPetContactDto(PetContact petContact);

    List<PetContact> petContactDtoListToPetContact(List<PetContactDto> petContactDtoList);

    List<PetContactDto> petContactListToPetContactDto(List<PetContact> petContactList);
}

