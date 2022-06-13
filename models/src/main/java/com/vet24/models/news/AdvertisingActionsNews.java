package com.vet24.models.news;

import com.vet24.models.enums.NewsType;
import lombok.EqualsAndHashCode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ADVERTISING_ACTIONS")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AdvertisingActionsNews extends News {

    private final NewsType type;
    public AdvertisingActionsNews() {
        super();
        this.type = NewsType.ADVERTISING_ACTIONS;

    }

    public AdvertisingActionsNews(String content, boolean isImportant, LocalDateTime endTime) {
        super(content, isImportant, endTime);
        this.type = NewsType.ADVERTISING_ACTIONS;
    }
}
