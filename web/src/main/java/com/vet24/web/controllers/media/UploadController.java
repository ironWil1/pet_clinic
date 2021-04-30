package com.vet24.web.controllers.media;

import com.vet24.service.media.UploadService;
import com.vet24.models.dto.UploadedFileDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload-file")
    @ApiResponse(responseCode = "200", description = "Successful upload", content = @Content(schema = @Schema(implementation = UploadedFileDto.class)))
    public ResponseEntity<UploadedFileDto> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(uploadService.store(file), HttpStatus.OK);
    }
}
