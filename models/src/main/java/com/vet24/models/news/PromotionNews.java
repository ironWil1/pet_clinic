package com.vet24.models.news;

import com.vet24.models.enums.NewsType;
import lombok.EqualsAndHashCode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("PROMOTION")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PromotionNews extends News {

    public PromotionNews() {
        super();
    }

    public PromotionNews(long id, NewsType type, String content, boolean isImportant, LocalDateTime endTime) {
        super(id, NewsType.PROMOTION, content, isImportant, endTime);
    }
}
