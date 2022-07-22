package com.vet24.models.dto.user;

import com.vet24.models.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientNewsResponseDto {
    private Long id;
    private String title;
    private NewsType type;
    private String content;
}
