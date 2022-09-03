package com.vet24.web.controllers.found;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.pet.PetContact;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PetFoundControllerTest extends ControllerAbstractIntegrationTest {
    private final String URL = "/api/petFound";
    private String token;

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
        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "Some text");
        Mockito.doNothing()
                .when(mailService)
                .sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(petFoundDto))
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        boolean isPetFound = entityManager.createQuery("SELECT CASE WHEN pf.longitude =: longitude " +
                        "THEN TRUE ELSE FALSE END FROM PetFound pf WHERE pf.longitude =: longitude", Boolean.class)
                .setParameter("longitude", petFoundDto.getLongitude()).getSingleResult();
        assertTrue("Тест не пройден! Найденный питомец отсутствует в БД!", isPetFound);
        verify(mailService, only()).sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
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
        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456787", "some text");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(petFoundDto))
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        boolean isPetNotFound = entityManager.createQuery("SELECT CASE WHEN COUNT(pf) = 0 " +
                        "THEN TRUE ELSE FALSE END FROM PetFound pf WHERE pf.longitude =: longitude", Boolean.class)
                .setParameter("longitude", petFoundDto.getLongitude()).getSingleResult();
        assertTrue("Тест не пройден! Найденный питомец найден в БД!", isPetNotFound);
        verify(mailService, never()).sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
        verifyNoInteractions(mailService);
    }

    //получение контактной информации - успешно
    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/controllers/petFoundController/user-entities.yml",
                    "/datasets/controllers/petFoundController/pet-entities.yml",
                    "/datasets/controllers/petFoundController/pet-contact.yml",
                    "/datasets/controllers/petFoundController/pet-found.yml"})
    public void testGetPetContactInfoSuccess() throws Exception {
        String code = "2C8B05A948803EA65B96C3E1DD4DCDDC";
        PetContact petContact = entityManager.createQuery("SELECT pc FROM PetContact pc WHERE pc.code =: code",
                PetContact.class).setParameter("code", code).getSingleResult();
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .param("code", code)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(petContact.getAddress()))
                .andExpect(jsonPath("$.ownerName").value(petContact.getOwnerName()))
                .andExpect(jsonPath("$.phone").value(petContact.getPhone()));
    }

    //получение контактной информации - ошибка 404

    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/controllers/petFoundController/user-entities.yml",
                    "/datasets/controllers/petFoundController/pet-entities.yml",
                    "/datasets/controllers/petFoundController/pet-contact.yml",
                    "/datasets/controllers/petFoundController/pet-found.yml"})
    public void testGetPetContactInfoError404Pet() throws Exception {
        String code = "2C8B05A948803EA65B96C3E1DD4DCDDX";
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        boolean isNotFoundPetContact = entityManager.createQuery("SELECT CASE WHEN COUNT(pc) = 0 " +
                        "THEN TRUE ELSE FALSE END FROM PetContact pc WHERE pc.code =: code", Boolean.class)
                .setParameter("code", code).getSingleResult();
        assertTrue("Тест не пройден! Результат найден по коду в базе!", isNotFoundPetContact);
    }
}
