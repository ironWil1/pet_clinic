package com.vet24.models.dto.news;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vet24.models.enums.NewsType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdatingNewsDto.class, name = "UPDATING"),
        @JsonSubTypes.Type(value = AdvertisingActionsNewsDto.class, name = "ADVERTISING_ACTIONS"),
        @JsonSubTypes.Type(value = DiscountsNewsDto.class, name = "DISCOUNTS"),
        @JsonSubTypes.Type(value = PromotionNewsDto.class, name = "PROMOTION"),

})
public abstract class AbstractNewNewsDto {

    private NewsType type;

    @NotBlank(message = "Поле content не должно быть пустым")
    private String content;

    boolean isImportant;

    @FutureOrPresent(message = "Поле endTime должно быть в будующем или настоящим временем")
    private LocalDateTime endTime;

    @JsonCreator
    protected AbstractNewNewsDto(NewsType type, String content, boolean isImportant, LocalDateTime endTime) {
        this.type = type;
        this.content = content;
        this.isImportant = isImportant;
        this.endTime = endTime;

    }

}
