package com.vet24.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.vet24.service.media.MailService;
import com.vet24.web.config.ClinicDBRider;
import com.vet24.web.controllers.user.AuthRequest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DBUnit(schema = "public", caseInsensitiveStrategy = Orthography.LOWERCASE)
@ClinicDBRider
public abstract class ControllerAbstractIntegrationTest {

    @MockBean
    protected MailService mailService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Nullable
    protected String getAccessToken(String email, String password) {
        String url = "http://localhost:8090/auth";
        AuthRequest authRequest = new AuthRequest(email, password);
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);
        try {
            ResponseEntity<String> responceEntity = template.postForEntity(url, authRequest, String.class);
            headers.setContentType(MediaType.APPLICATION_JSON);
            responceEntity = template.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            String response = responceEntity.getBody();
            return response.contains("jwtToken") ? response.substring(13, response.length() - 2) : null;
        } catch (RestClientException e) {
            return null;
        }
    }
}
