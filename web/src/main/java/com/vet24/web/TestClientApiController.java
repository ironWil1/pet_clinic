package com.vet24.web;

import com.vet24.models.TestClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/client")
public class TestClientApiController {
    @PostMapping
    public TestClient testClient(@RequestBody TestClient testClient) {
        return testClient;
    }
}
