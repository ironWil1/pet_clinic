package com.vet24.discord.models.dto.discord.embed;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class EmbedImageDto {

    private String url;

    private String proxy_url;

    private Integer width;

    private Integer height;
}
