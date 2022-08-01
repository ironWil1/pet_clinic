package com.vet24.discord.models.dto.discord.embed;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class EmbedFieldDto {

    private String name;

    private String value;

    private boolean inline;
}
