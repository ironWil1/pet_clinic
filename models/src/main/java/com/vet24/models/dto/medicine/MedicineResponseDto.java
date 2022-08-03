package com.vet24.models.dto.medicine;

import com.vet24.models.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MedicineResponseDto {
    Long id;
    String manufactureName;
    String name;
    String iconUrl;
    String description;
    LocalDateTime creationDateTime;
    LocalDateTime lastUpdateDateTime;
    UserInfoDto createAuthor;
    UserInfoDto lastUpdateAuthor;
}
