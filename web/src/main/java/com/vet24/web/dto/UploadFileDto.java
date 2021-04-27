package com.vet24.web.dto;

public class UploadFileDto {

    private String filename;
    private String url;

    public UploadFileDto() {
    }

    public UploadFileDto(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
