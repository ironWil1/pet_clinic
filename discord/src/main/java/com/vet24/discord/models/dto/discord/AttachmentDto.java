package com.vet24.discord.models.dto.discord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class AttachmentDto {

    private Long id;

    private String filename;

    private String description;

    private String content_type;

    private Integer size;

    private String url;

    private String proxy_url;

    private Integer height;

    private Integer width;

    private boolean ephemeral;

}
