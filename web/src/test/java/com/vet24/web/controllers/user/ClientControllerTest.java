package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.Principal;

@DBRider
@WithUserDetails(value = "client1@email.com")
@DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client";

    private final Principal principal = () -> "client1@email.com";

    @Test
    public void shouldGetResponseEntityClientDto_ForCurrentClient() {
        ResponseEntity<ClientDto> response = testRestTemplate.getForEntity(URI, ClientDto.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(principal.getName(), response.getBody().getEmail());
    }

    @Test
    public void uploadClientAvatar() {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", classPathResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<UploadedFileDto> response = testRestTemplate.postForEntity(URI + "/avatar", requestEntity, UploadedFileDto.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /*
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getClientAvatar() {
        persistClientAvatar();
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/avatar", byte[].class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistClientAvatar() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/avatar", HttpMethod.POST, entity, String.class, 3);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    */
}