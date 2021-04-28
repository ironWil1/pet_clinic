package com.vet24.service.media;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String store(MultipartFile file);
}
