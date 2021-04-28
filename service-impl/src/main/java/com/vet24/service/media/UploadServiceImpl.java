package com.vet24.service.media;

import com.vet24.models.exception.StorageException;
import com.vet24.service.media.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class UploadServiceImpl implements UploadService {

    @Override
    public String store(MultipartFile file) {
        String originFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + originFilename);
        }

        LocalDateTime now = LocalDateTime.now();
        int extensionIndex = originFilename.lastIndexOf(".");
        String storagePath = "uploads/" + now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
        String storageFilename = originFilename.substring(0, extensionIndex)
                + now.format(DateTimeFormatter.ofPattern("-yyyyMMddHHmmss"))
                + originFilename.substring(extensionIndex);

        try (InputStream inputStream = file.getInputStream()) {

            if (!Files.exists(Paths.get(storagePath))) {
                Files.createDirectories(Paths.get(storagePath));
            }
            Files.copy(inputStream, Paths.get(storagePath + storageFilename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originFilename);
        }

        return storagePath + storageFilename;
    }
}
