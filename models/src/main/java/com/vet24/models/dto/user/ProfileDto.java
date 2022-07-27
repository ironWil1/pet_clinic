package com.vet24.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private String avatarUrl;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String discordId;

    private String telegramId;



}
