package com.vet24.models.dto.user;

import com.vet24.models.dto.pet.PetResponseDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ClientDto {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    private String avatar;

    @Email
    private String email;

    private List<PetResponseDto> pets;
}
