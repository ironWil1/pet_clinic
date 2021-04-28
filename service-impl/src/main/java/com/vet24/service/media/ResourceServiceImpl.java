package com.vet24.service.media;

import com.vet24.models.exception.StorageException;
import com.vet24.service.media.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public byte[] loadAsByteArray(String filename) {
        filename = "uploads/" + filename;

        try (FileInputStream fis = new FileInputStream(filename)) {
            return StreamUtils.copyToByteArray(fis);
        } catch (IOException e) {
            throw new StorageException("Could not read file: " + filename);
        }
    }
}
