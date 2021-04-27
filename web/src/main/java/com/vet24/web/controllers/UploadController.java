package com.vet24.web.controllers;

import com.vet24.service.ResourceService;
import com.vet24.service.UploadService;
import com.vet24.web.dto.UploadFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final ResourceService resourceService;
    private final UploadService uploadService;

    @Autowired
    public UploadController(ResourceService resourceService, UploadService uploadService) {
        this.resourceService = resourceService;
        this.uploadService = uploadService;
    }

    @GetMapping("/{path: \\d{4}/\\d{1,2}/\\d{1,2}/\\S*}")
    public ResponseEntity<Resource> getFile(@PathVariable String path){
        Resource file = resourceService.loadAsResource(path);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PostMapping("/upload-file")
    public ResponseEntity<UploadFileDto> uploadFile(@RequestParam("file") MultipartFile file){
        String storageUrl = uploadService.store(file);

        UploadFileDto uploadFile = new UploadFileDto(file.getOriginalFilename(), storageUrl);

        return new ResponseEntity<>(uploadFile, HttpStatus.OK);
    }
}
