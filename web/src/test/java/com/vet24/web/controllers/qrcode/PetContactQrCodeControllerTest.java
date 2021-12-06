package com.vet24.web.controllers.qrcode;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetContactDao;
import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PetContactQrCodeControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client/pet/{petId}/qr";
    private String token;

    @Autowired
    PetContactDao petContactDao;

    PetContactDto petContactDtoNew = new PetContactDto("Петр", "Пущино 39", 89267777777L);
    PetContactDto petContactDto1 = new PetContactDto("Мария", "Невского 17", 4854789899L);
    PetContactDto petContactDto2 = new PetContactDto("Ираида", "Кастанаевская 45", 84951447200L);

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com","client");
    }

    // +mock, get create qr code for petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreateQrCodeForViewOnePetContactSuccess() throws Exception {
        int beforeCount = petContactDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 107)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petContactDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(petContactDao.getAll().size());
    }

    // +mock, get pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreateQrCodeForViewOnePetContactError404Pet() throws Exception {
        int beforeCount = petContactDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petContactDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(petContactDao.getAll().size());
    }

    // +mock, post update petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testUpdatePetContactForPetSuccess() throws Exception {
        int beforeCount = petContactDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 105)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petContactDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(beforeCount).isEqualTo(petContactDao.getAll().size());
    }

    // +mock, post create petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreatePetContactForPetSuccess() throws Exception {
        int beforeCount = petContactDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 106)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petContactDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(petContactDao.getAll().size());
    }

    // +mock, post pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testUpdateAndCreatePetContactForPetError404Pet() throws Exception {
        int beforeCount = petContactDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petContactDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(petContactDao.getAll().size());
    }
}
