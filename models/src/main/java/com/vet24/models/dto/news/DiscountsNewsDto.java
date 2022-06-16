package com.vet24.models.dto.news;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.NewsType;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
public class DiscountsNewsDto extends AbstractNewNewsDto {

    @JsonCreator
    public DiscountsNewsDto(String content, boolean isImportant, LocalDateTime endTime) {
        super(NewsType.DISCOUNTS, content, isImportant, endTime);
    }
}
