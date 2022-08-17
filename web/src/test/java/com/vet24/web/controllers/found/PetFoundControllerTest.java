package com.vet24.web.controllers.found;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetFoundDao;
import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.pet.PetContact;
import com.vet24.service.media.MailService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class PetFoundControllerTest extends ControllerAbstractIntegrationTest {

    private final String URL = "/api/petFound";
    private String token;
    @Autowired
    private MailService mailService;

    @Autowired
    private PetFoundDao petFoundDao;


    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    // Сохранение данных найденного питомца и создание с отправкой владельцу сообщения о питомце - успешно
    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/controllers/petFoundController/user-entities.yml",
                    "/datasets/controllers/petFoundController/pet-entities.yml",
                    "/datasets/controllers/petFoundController/pet-contact.yml",
                    "/datasets/controllers/petFoundController/pet-found.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessage() throws Exception {
        String code = "57747D2DAEB3397A1BD2F2313E67891E";
        int sizeBefore = petFoundDao.getAll().size();
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
        assertThat(++sizeBefore).isEqualTo(petFoundDao.getAll().size());

    }

    // Сохранение данных найденного питомца и создание с отправкой владельцу сообщения о питомце - ошибка 404
    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/controllers/petFoundController/user-entities.yml",
                    "/datasets/controllers/petFoundController/pet-entities.yml",
                    "/datasets/controllers/petFoundController/pet-contact.yml",
                    "/datasets/controllers/petFoundController/pet-found.yml"})
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

    //получение контактной информации - успешно
    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/controllers/petFoundController/user-entities.yml",
                    "/datasets/controllers/petFoundController/pet-entities.yml",
                    "/datasets/controllers/petFoundController/pet-contact.yml",
                    "/datasets/controllers/petFoundController/pet-found.yml"})
    public void testGetPetContaсtInfoSuccess() throws Exception {
        String code = "2C8B05A948803EA65B96C3E1DD4DCDDC";
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .param("code", code)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.address").value("Стрелковая 70"))
                .andExpect(jsonPath("$.ownerName").value("Александр"))
                .andExpect(jsonPath("$.phone").value(89629691030L));
    }

    //получение контактной информации - ошибка 404

    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/controllers/petFoundController/user-entities.yml",
                    "/datasets/controllers/petFoundController/pet-entities.yml",
                    "/datasets/controllers/petFoundController/pet-contact.yml",
                    "/datasets/controllers/petFoundController/pet-found.yml"})
    public void testGetPetContaсtInfoEror404Pet() throws Exception {
        String code = "2C8B05A948803EA65B96C3E1DD4DCDDX";
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
