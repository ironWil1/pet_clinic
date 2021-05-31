package com.vet24.models.dto.user;

import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class ClientDto {

    private String firstname;
    private String lastname;
    private String avatar;
    private String email;
    private Set<PetDto> pets;
}
