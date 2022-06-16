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
@DiscriminatorValue("UPDATING")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UpdatingNews extends News {

    public UpdatingNews() {
        super();
    }

    public UpdatingNews(String content, boolean isImportant, LocalDateTime endTime) {
        super(NewsType.UPDATING, content, isImportant, endTime);
    }
}

