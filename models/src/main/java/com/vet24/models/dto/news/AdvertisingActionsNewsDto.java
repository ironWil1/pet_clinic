package com.vet24.models.dto.news;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.NewsType;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
public class AdvertisingActionsNewsDto extends NewsDto {

    @JsonCreator
    public AdvertisingActionsNewsDto(long id, String content, boolean isImportant, LocalDateTime endTime) {
        super(id, NewsType.ADVERTISING_ACTIONS, content, isImportant, endTime);
    }
}
