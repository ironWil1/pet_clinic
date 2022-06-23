package com.vet24.models.news;

import com.vet24.models.enums.NewsType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("PROMOTION")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PromotionNews extends News {
    public PromotionNews() {
        super();
    }
    public PromotionNews(String content, boolean isImportant, LocalDateTime endTime) {
        super(NewsType.PROMOTION, content, isImportant, endTime);
    }
}
