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

    public AdvertisingActionsNews() {
        super();
    }

    public AdvertisingActionsNews(long id, NewsType type, String content, boolean isImportant, LocalDateTime endTime) {
        super(id, NewsType.ADVERTISING_ACTIONS, content, isImportant, endTime);
    }
}
