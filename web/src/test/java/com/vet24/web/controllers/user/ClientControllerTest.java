package com.vet24.web.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.sun.xml.bind.v2.TODO;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.models.pet.Pet;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private ClientController controller;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private PetService petService;

    @Autowired
    private ClientService clientService;

    private final String URI = "http://localhost:8090/api/client";

    final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private final Principal principal = new Principal() {
        @Override
        public String getName() {
            return "client1@email.com";
        }
    };

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    @WithUserDetails("client1@email.com")
    @Transactional
    public void getCurrentClient() {

        ResponseEntity<String> result = testRestTemplate.withBasicAuth("client1@email.com", "client")
                .getForEntity(URI, String.class);
        String responseBody = result.getBody();
        System.err.println(responseBody);
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());

        /*ClientDto clientDto = clientMapper.clientToClientDto(clientService.getClientByEmail("client1@email.com"));
        ResponseEntity<String> response = testRestTemplate.getForEntity(URI, String.class);
        String responseBody = response.getBody();
        System.out.println(responseBody);
        assertThat(clientDto).isNotNull();
        Assert.assertEquals(clientDto, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());*/
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    @Transactional
    @WithUserDetails("client1@email.com")
    public void getCurrentClientnew() throws Exception {
        //TODO Почему секьюрити требует что бы существовали одновременно @Dataset и Postgre сущности?
        //TODO Почему Клиенту не подгружается сущность Pet? На обычном сервере всё хорошо работает...
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("client1@email.com")))
                .andDo(MockMvcResultHandlers.print()).andReturn();
        String content = result.getResponse().getContentAsString();
        System.err.println(content);
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
    @WithUserDetails("client1@email.com")
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
}