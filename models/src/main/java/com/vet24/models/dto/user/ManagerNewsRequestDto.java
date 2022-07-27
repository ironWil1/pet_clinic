package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.NewsType;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerNewsRequestDto {
    private String title;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Поле content не должно быть пустым")
    private String content;
    private NewsType type;
    private boolean isImportant;
    private LocalDateTime endTime;
}
