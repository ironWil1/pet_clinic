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
@DiscriminatorValue("DISCOUNTS")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class DiscountsNews extends News {

    public DiscountsNews() {
        super();

    }

    public DiscountsNews(String content, boolean isImportant, LocalDateTime endTime) {
        super(NewsType.DISCOUNTS, content, isImportant, endTime);

    }
}
