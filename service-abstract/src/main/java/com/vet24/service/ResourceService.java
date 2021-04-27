package com.vet24.service;

import org.springframework.core.io.Resource;

public interface ResourceService {

    Resource loadAsResource(String filename);
}
