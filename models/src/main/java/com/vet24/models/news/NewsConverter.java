package com.vet24.models.news;

import com.vet24.models.enums.NewsType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class NewsConverter implements AttributeConverter<NewsType, String> {

    @Override
    public String convertToDatabaseColumn(NewsType newsType) {
        if (newsType == null) {
            return null;
        }
        return newsType.getCode();
    }

    @Override
    public NewsType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(NewsType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

