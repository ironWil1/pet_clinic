package com.vet24.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String store(MultipartFile file);
}
