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

    final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private PetContactQrCodeController petContactQrCodeController;

    @Test
    public void createQrCodeForViewOnePetContactOrNotFoundIdForPetContact() throws Exception {
        final String URL_GET_PET_CONTACT = "/api/client/pet/1/qr";
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_GET_PET_CONTACT)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        final String URL_GET_NOT_FOUND_ID_PET_CONTACT = "/api/client/pet/1000/qr";
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_GET_NOT_FOUND_ID_PET_CONTACT))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateOldAndSaveNewPetContactForPetOrNotFoundIdForPet() throws Exception {
        final String URL_POST_UPDATE = "/api/client/pet/1/qr";
        PetContactDto petContact1 = new PetContactDto("Мария", "Невского 17", "4854789899");
        String bodyUpdate = (new ObjectMapper()).valueToTree(petContact1).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST_UPDATE)
                .content(bodyUpdate).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        final String URL_POST_CREATE = "/api/client/pet/7/qr";
        PetContactDto petContact2 = new PetContactDto("Мария", "Невского 17", "5647564343");
        String bodyCreate = (new ObjectMapper()).valueToTree(petContact2).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST_CREATE)
                .content(bodyCreate).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        final String URL_POST_NOT_FOUND_ID = "/api/client/pet/1000/qr";
        PetContactDto petContact3 = new PetContactDto("Мария", "Невского 17", "2456786957");
        String body = (new ObjectMapper()).valueToTree(petContact3).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST_NOT_FOUND_ID)
                .content(body).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
