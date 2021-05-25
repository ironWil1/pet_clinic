package com.vet24.models.dto.user;

import com.vet24.models.dto.pet.PetDto;
import lombok.Data;

import java.util.Set;

@Data
public class ClientDto {

    private String avatar;
    private String email;
    private Set<PetDto> pets;
}
