package com.vet24.models.dto.news;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.NewsType;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
public class DiscountsNewsDto extends NewsDto {

    @JsonCreator
    public DiscountsNewsDto(long id, String content, boolean isImportant, LocalDateTime endTime) {
        super(id, NewsType.DISCOUNTS, content, isImportant, endTime);
    }
}
