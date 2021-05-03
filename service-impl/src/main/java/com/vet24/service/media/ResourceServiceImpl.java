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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@PropertySource("application.properties")
public class ResourceServiceImpl implements ResourceService {

    @Value("${application.upload.folder:uploads}")
    private String uploadFolder;

    private String contentTypeDefault = "application/octet-stream";

    private Map<String, String> contentTypeMap = Stream.of(new String[][]{
            {".mp4", "video/mp4"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".ogg", "video/ogg"},
            {".3gpp", "video/3gpp"},
            {".wmv", "video/x-ms-wmv"},
            {".flv", "video/x-flv"},
            {".jpeg", "image/jpeg"},
            {".jpe", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".gif", "image/gif"},
            {".png", "image/png"},
            {".tiff", "image/tiff"},
            {".tif", "image/tiff"},
            {".mid", "audio/midi"},
            {".midi", "audio/midi"},
            {".mp3", "audio/mpeg"},
            {".wav", "audio/vnd.wav"},
            {".flac", "audio/flac"},
            {".m4a", "audio/mp4"},
            {".m4b", "audio/mp4"},
            {".m3u", "audio/x-mpegurl"},
            {".au", "audio/basic"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

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
            throw new StorageException("File not found: " + filename, e);
        }
    }
}
