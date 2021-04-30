package com.vet24.service.media;

import com.vet24.models.dto.UploadedFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    UploadedFileDto store(MultipartFile file);
}
