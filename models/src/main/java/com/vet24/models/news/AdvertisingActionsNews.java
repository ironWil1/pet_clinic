package com.vet24.models.news;

import com.vet24.models.enums.NewsType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ADVERTISING_ACTIONS")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AdvertisingActionsNews extends News {
    public AdvertisingActionsNews() {
        super();
    }
    public AdvertisingActionsNews(String content, boolean isImportant, LocalDateTime endTime) {
        super(NewsType.ADVERTISING_ACTIONS, content, isImportant, endTime);
    }


}
