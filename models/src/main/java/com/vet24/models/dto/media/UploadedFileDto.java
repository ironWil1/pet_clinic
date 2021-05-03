package com.vet24.models.dto.media;

import lombok.Data;

@Data
public class UploadedFileDto {
    private String filename;
    private String url;

    public UploadedFileDto(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }
}
