package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DBRider
@WithUserDetails(value = "client1@email.com")
@DataSet(cleanBefore = true,
        value = {"/datasets/registration.yml",
        "/datasets/user-entities.yml",
        "/datasets/pet-entities.yml"})
public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    // TODO: remove "..localhost"
    private final String URI = "http://localhost:8090/api/client";

    // TODO: clientPrincipal
    private final Principal principal = () -> "client1@email.com";

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private PetService petService;
    @Autowired
    private ClientService clientService;

    // TODO: преписать на RestTemplate
    @Test
    public void shouldGetClientDto_ForCurrentClient() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(principal.getName())))
//                .andExpect(jsonPath("$.avatar", is("test.png")))
                .andExpect(jsonPath("$.email", is(principal.getName())))
                .andExpect(jsonPath("$.pets[0].id", is(107)))
                .andExpect(jsonPath("$.pets[0].name").value("Старый добрый пес"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
        System.err.println(result.getResponse().getContentType());

        ClientDto clientDto = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ClientDto.class);
        System.err.println(clientDto);

        //System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void shouldGetResponseEntityClientDto_ForCurrentClient() {
        ResponseEntity<ClientDto> response = testRestTemplate.getForEntity(URI, ClientDto.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getEmail(), is(principal.getName()));
        assertThat(response.getBody(), isA(ClientDto.class));

        // TODO: failed to lazily initialize a collection of role: com.vet24.models.user.Client.pets, could not initialize proxy - no Session
//        ClientDto clientDto = clientMapper.clientToClientDto(clientService.getClientByEmail(principal.getName()));
//        Assert.assertEquals(clientDto, response.getBody());
    }

    @Test
    public void actionsWithClientAvatar() throws Exception {

//        ClassPathResource classPathResource = new ClassPathResource("test.png");
//        System.err.println("getDescription " + classPathResource.getDescription());
//        System.err.println("getURL " + classPathResource.getURL());

        // 1. проверить, что нету (404)
//        ResponseEntity<byte[]> response = testRestTemplate.getForEntity(URI + "/avatar", byte[].class);
//        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/avatar")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                //.andExpect(MockMvcResultMatchers.header().stringValues("count", "150"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print()).andReturn();

/*
        //mockMvc.perform(MockMvcRequestBuilders.post(URI + "/avatar", )

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        //ResponseEntity<String> response = testRestTemplate
        //        .exchange(URI + "/avatar", HttpMethod.POST, entity, String.class, 3);

        //Assert.assertEquals(HttpStatus.OK, response.getStatusCode());


        MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> response = restTemplate.postForEntity(URI + "/avatar", entity, String.class, 3);
        //assertThat(response.getStatusCode(), is(HttpStatus.OK));
        //assertThat(response.getBody(), notNullValue());
        //assertThat(response.getBody(), isA(UploadedFileDto.class));
        //System.err.println(response);

*/

        /*ResponseEntity<byte[]> response;

        try {
            response = restTemplate.getForEntity(URI + "/avatar", byte[].class);
            assertThat(response.getHeaders(), hasValue(MediaType.APPLICATION_JSON_VALUE));
        } catch (HttpClientErrorException e) {
            //assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        }*/

        // 2. добавить

        // 3. проверить, что есть
//        response = restTemplate.getForEntity(URI + "/avatar", byte[].class);
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(response.getBody(), notNullValue());

        // 4. как удалить?
    }

    //@Test
    public void getClientAvatar() {
        persistClientAvatar();
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/avatar", byte[].class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //@Test
    public void persistClientAvatar() {
//        MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ClassPathResource classPathResource = new ClassPathResource("test.png");
//        System.err.println("classPathResource " + classPathResource.exists() + " =====");
        body.add("file", classPathResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.add("Accept", "application/json");
//        headers.add("Content-Type", "image/png");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

//        System.err.println("getBody== " + requestEntity.getBody().get("file") + " =====");
//        System.err.println("hasBody== " + requestEntity.hasBody() + " =====");

        //ResponseEntity<String> response = restTemplate.exchange(URI + "/avatar", HttpMethod.POST, requestEntity, String.class, 3);
//        TestRestTemplate testRestTemplate1 = new TestRestTemplate("client1@email.com", "client", TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
        ResponseEntity<String> response = testRestTemplate.postForEntity(URI + "/avatar", requestEntity, String.class);
        //String response = restTemplate.postForObject(URI + "/avatar", requestEntity, String.class);

//        System.err.println("response== " + response + " =====");

//        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}