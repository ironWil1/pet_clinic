package com.vet24.web.controllers.media;

import com.vet24.service.media.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uploads")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/{year}/{month}/{day}/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable int year, @PathVariable int month, @PathVariable int day, @PathVariable String filename){
        String path = year + "/" + month + "/" + day + "/" + filename;
        byte[] file = resourceService.loadAsByteArray(path);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }
}
