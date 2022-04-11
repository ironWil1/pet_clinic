package com.vet24.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class DoctorDtoPost {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String avatar;

}
