package com.vet24.models.dto.user;

import com.vet24.models.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerNewsRequestDto {
    @NotBlank(message = "Поле title не должно быть пустым")
    private String title;

    @NotBlank(message = "Поле content не должно быть пустым")
    private String content;

    @NotNull(message = "Поле type не должно быть пустым")
    private NewsType type;

    @NotNull(message = "Поле isImportant не должно быть пустым")
    private boolean isImportant;

    @NotNull(message = "Поле endTime не должно быть пустым")
    private LocalDateTime endTime;
}
