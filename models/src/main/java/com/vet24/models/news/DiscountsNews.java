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

    private  final NewsType type;
    public DiscountsNews() {
        super();
        this.type = NewsType.DISCOUNTS;
    }

    public DiscountsNews(String content, boolean isImportant, LocalDateTime endTime) {
        super(content, isImportant, endTime);
        this.type = NewsType.DISCOUNTS;
    }
}
