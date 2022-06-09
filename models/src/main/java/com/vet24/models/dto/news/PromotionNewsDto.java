package com.vet24.models.dto.news;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.NewsType;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
public class PromotionNewsDto extends AbstractNewNewsDto {

    @JsonCreator
    public PromotionNewsDto(NewsType type, String content, boolean isImportant, LocalDateTime endTime) {
        super(NewsType.PROMOTION, content, isImportant, endTime);
    }
}