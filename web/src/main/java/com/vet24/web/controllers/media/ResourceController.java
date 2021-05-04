package com.vet24.web.controllers.media;

import com.vet24.service.media.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/uploads")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/{year:\\d{4}}/{month:\\d{2}}/{day:\\d{2}}/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String filename) {
        byte[] file = resourceService.loadAsByteArray(year + File.separator + month + File.separator + day + File.separator + filename);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", resourceService.getContentTypeByFileName(filename));

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
}
