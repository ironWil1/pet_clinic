package com.vet24.discord.models.dto.discord.embed;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class EmbedFooterDto {

    private String text;

    private String icon_url;

    private String proxy_icon_url;

}
