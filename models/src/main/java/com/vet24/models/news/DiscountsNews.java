package com.vet24.models.news;

import com.vet24.models.enums.NewsType;
import lombok.EqualsAndHashCode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("DISCOUNTS")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class DiscountsNews extends News {

    public DiscountsNews() {
        super();
    }

    public DiscountsNews(long id, NewsType type, String content, boolean isImportant, LocalDateTime endTime) {
        super(id, NewsType.DISCOUNTS, content, isImportant, endTime);
    }
}
