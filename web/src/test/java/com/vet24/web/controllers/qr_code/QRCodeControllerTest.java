package com.vet24.web.controllers.qr_code;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.mappers.PetContactMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.service.pet.PetContactService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;


import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder
public class QRCodeControllerTest extends ControllerAbstractIntegrationTest {

    final String URL_GET = "/api/client/pet/1/qr";
    final String URL_POST = "/api/client/pet/1/qr";
    final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private QRCodeController qrCodeController;

    /*@Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }*/

    @Test
    public void getQRCodeController() {
        assertThat(qrCodeController).isNotNull();
    }

    @Test
    public void createZxingQRCode() throws Exception {
        this.mockMvc.perform(get(URL_GET)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void savePetContact() throws Exception {
        PetContactDto petContact = new PetContactDto("Мария", "Невского 17", "4854789899");
        String body = (new ObjectMapper()).valueToTree(petContact).toString();
        this.mockMvc.perform(post(URL_POST).content(body).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isCreated()).andDo(print());
    }
}
