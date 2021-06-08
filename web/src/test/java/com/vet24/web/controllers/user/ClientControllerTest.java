package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.models.user.Client;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
public class ClientControllerTest extends ControllerAbstractIntegrationTest {


    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientService clientService;



    private final String URI = "http://localhost:8090/api/client";
    private final String URI_LIKE = "http://localhost:8090/api/client/{commentId}/{dis}";

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getCurrentClient() {
        ClientDto clientDto = clientMapper.clientToClientDto(clientService.testGetCurrentClientWithPets());
        ResponseEntity<ClientDto> response = testRestTemplate
                .getForEntity(URI, ClientDto.class);

        assertThat(clientDto).isNotNull();
        Assert.assertEquals(clientDto, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

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

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/registration-doctor.yml", "/datasets/comments.yml"})
    public void shouldBeNotFoundComment(){

        Client client = clientService.getCurrentClient();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI_LIKE , HttpMethod.POST,entity,Void.class,10000L,false);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/registration-doctor.yml", "/datasets/comments.yml"})
    public void shouldBeLikedComment(){

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI_LIKE , HttpMethod.POST,entity,Void.class,1L,false);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Client client = clientService.testGetCurrentClientWithLikes();
        Assert.assertEquals(client.getLikes().size(), 1);
        Assert.assertEquals(client.getLikes().get(0).getDislike(), false);

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/registration-doctor.yml", "/datasets/comments.yml"})
    public void shouldBeDislikedComment(){

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI_LIKE , HttpMethod.POST,entity,Void.class,2L,true);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Client client = clientService.testGetCurrentClientWithLikes();
        Assert.assertEquals(client.getLikes().size(), 1);
        Assert.assertEquals(client.getLikes().get(0).getDislike(), true);

    }
}