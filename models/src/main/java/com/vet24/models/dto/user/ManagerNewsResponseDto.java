package com.vet24.models.dto.user;

import com.vet24.models.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerNewsResponseDto {
    private long id;
    private String title;
    private String content;
    private NewsType type;
    private boolean isImportant;
    private LocalDateTime endTime;
    private boolean published;
    private List<String> pictures;
}
