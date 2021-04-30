package com.vet24.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientDto {

    private String username;
    private String avatar;
    private String email;
    private List<PetDto> pets;
}
