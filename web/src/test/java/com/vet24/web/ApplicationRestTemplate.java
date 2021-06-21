package com.vet24.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Profile("TestProfile")
@Component
public class ApplicationRestTemplate extends RestTemplate {

    @Autowired
    protected MockMvc mockMvc;

    @PostConstruct
    public void construct() {
        MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
        this.setRequestFactory(requestFactory);
        this.setErrorHandler(new RestTemplateResponseErrorHandler());
    }
}
