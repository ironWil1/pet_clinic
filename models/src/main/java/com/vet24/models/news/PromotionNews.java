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
    private NewsType type;
    public PromotionNews() {
        super();
        this.type = NewsType.PROMOTION;
    }
    public PromotionNews(String content, boolean isImportant, LocalDateTime endTime) {
        super( content, isImportant, endTime);
        this.type = NewsType.PROMOTION;
    }
}
