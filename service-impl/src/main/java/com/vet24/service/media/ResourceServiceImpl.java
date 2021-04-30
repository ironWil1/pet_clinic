package com.vet24.service.media;

import com.vet24.models.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("application.properties")
public class ResourceServiceImpl implements ResourceService {

    @Value("${application.upload.folder:uploads}")
    private String uploadFolder;

    private String contentTypeDefault = "application/octet-stream";

    private Map<String, String> contentTypeMap = new HashMap<>() {{
        put(".mp4", "video/mp4");
        put(".mpeg", "video/mpeg");
        put(".mpg", "video/mpeg");
        put(".ogg", "video/ogg");
        put(".3gpp", "video/3gpp");
        put(".wmv", "video/x-ms-wmv");
        put(".flv", "video/x-flv");
        put(".jpeg", "image/jpeg");
        put(".jpe", "image/jpeg");
        put(".jpg", "image/jpeg");
        put(".gif", "image/gif");
        put(".png", "image/png");
        put(".tiff", "image/tiff");
        put(".tif", "image/tiff");
        put(".mid", "audio/midi");
        put(".midi", "audio/midi");
        put(".mp3", "audio/mpeg");
        put(".wav", "audio/vnd.wav");
        put(".flac", "audio/flac");
        put(".m4a", "audio/mp4");
        put(".m4b", "audio/mp4");
        put(".m3u", "audio/x-mpegurl");
        put(".au", "audio/basic");
    }};

    @Override
    public String getContentTypeByFileName(String filename) {
        int extensionIndex = filename.lastIndexOf(".");

        if (extensionIndex < 0) {
            throw new StorageException("Cannot load file [" + filename + "] without extension");
        }

        return contentTypeMap.getOrDefault(
                filename.substring(extensionIndex),
                contentTypeDefault
        );
    }

    @Override
    public byte[] loadAsByteArray(String filename) {
        try (InputStream is = new FileSystemResource(uploadFolder + File.separator + filename).getInputStream()) {
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }
}
