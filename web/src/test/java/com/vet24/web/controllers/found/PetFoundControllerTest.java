package com.vet24.web.controllers.found;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetFoundDao;
import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.pet.PetContact;
import com.vet24.service.media.MailService;
import com.vet24.service.pet.PetContactService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;



public class PetFoundControllerTest extends ControllerAbstractIntegrationTest {

    private final String URL = "/api/petFound";
    private String token;

    @Autowired
    private PetContactService petContactService;
    @Autowired
    private MailService mailService;

    @Autowired
    private PetFoundDao petFoundDao;

    private  PetFoundDto petFoundDto;


    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    // Сохранение данных найденного питомца и создание с отправкой владельцу сообщения о питомце - успешно
    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/pet-found.yml",
                    "/datasets/pet-contact.yml",
                    "/datasets/user-entities.yml",
                    "/datasets/pet-entities.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessage() throws Exception {
        PetContact petContact = petContactService.getByKey(100L);
        String code = petContact.getCode();

        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "Some text");
        String bodyUpdate = objectMapper.valueToTree(petFoundDto).toString();
        Mockito.doNothing()
                .when(mailService)
                .sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(bodyUpdate).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("code", code))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    // Сохранение данных найденного питомца и создание с отправкой владельцу сообщения о питомце - ошибка 404
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-found.yml", "/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessageError404Pet() throws Exception {
        String code = "CD0964F7A769B65E2BA57822840B0E53";

        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "some text");
        String bodyUpdate = objectMapper.valueToTree(petFoundDto).toString();
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(bodyUpdate).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("code", code))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito.verify(mailService, times(0))
                .sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
    }
}
