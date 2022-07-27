package com.vet24.models.dto.news;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Long id;
    private NewsType type;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Поле content не должно быть пустым")
    private String content;
    private boolean isImportant;
    private LocalDateTime endTime;
}
