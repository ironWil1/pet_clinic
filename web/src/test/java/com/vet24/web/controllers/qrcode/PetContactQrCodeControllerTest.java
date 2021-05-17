package com.vet24.web.controllers.qrcode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

@FixMethodOrder
public class PetContactQrCodeControllerTest extends ControllerAbstractIntegrationTest {

    final String URL_GET = "/api/client/pet/1/qr";
    final String URL_POST = "/api/client/pet/7/qr";
    final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private PetContactQrCodeController petContactQrCodeController;

    @Test
    public void createZxingQRCode() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_GET)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void savePetContact() throws Exception {
        PetContactDto petContact = new PetContactDto("Мария", "Невского 17", "4854789899");
        String body = (new ObjectMapper()).valueToTree(petContact).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST).content(body).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
    }
}
