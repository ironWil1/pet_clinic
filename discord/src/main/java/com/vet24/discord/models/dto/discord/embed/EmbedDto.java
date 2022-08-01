package com.vet24.discord.models.dto.discord.embed;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Getter
@Setter
public class EmbedDto {

    private EmbedType type;    /// тип Embed вложения

    private String url;             /// ссылка в заголовке Embed

    private String title;           /// заголовок Embed

    private String description;     /// Текст вложенного Embed

    private Integer color;          /// Цвет Embed блока

    private String timestamp;       /// Время публикации

    private EmbedFieldDto[] fields;     ///  Дополнительные абзацы для форматирования Embed блока

    private EmbedImageDto image;        /// Изображение вложенное в Embed

    private EmbedThumbnailDto thumbnail;    /// Картинка - миниатюра рядом с заголовком Embed блока

    private EmbedFooterDto footer;  /// футер Embed блока (можно добавить текст и иконку)

}
