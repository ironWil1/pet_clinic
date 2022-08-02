package com.vet24.web.controllers.pet;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.org.hamcrest.core.Is;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PetContactControllerTest extends ControllerAbstractIntegrationTest {

    private PetContactDto updatedPetContactDto;
    private PetContactDto petContactDtoWithEmptyFirstField;
    private PetContactDto petContactDtoWithEmptySecondField;
    private PetContactDto petContactDtoWithEmptyThirdField;
    private PetContactDto petContactDtoWithEmptyFourthField;
    private final String URL = "/api/client/pet/contact";
    private String token;

    @Before
    public void updatePetContact() {
        this.updatedPetContactDto = new PetContactDto("Максим", "Липовая 3", 86967774242L, "Updated description");
        this.petContactDtoWithEmptyFirstField = new PetContactDto("", "Липовая 3", 86967774242L, "Updated description");
        this.petContactDtoWithEmptyFirstField = new PetContactDto("Максим", "", 86967774242L, "Updated description");
        this.petContactDtoWithEmptyFirstField = new PetContactDto("Максим", "Липовая 3", null, "Updated description");
        this.petContactDtoWithEmptyFirstField = new PetContactDto("Максим", "Липовая 3", 86967774242L, "");
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void getPetContactSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .param("petId", "107")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.address").value("Стрелковая 70"))
                .andExpect(jsonPath("$.ownerName").value("Александр"))
                .andExpect(jsonPath("$.phone").value(89629691030L))
                .andExpect(jsonPath("$.description").value("Description 107"));
    }

    // Питомец не пренадлежит текущему клиенту для get запроса у PetContact
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void getPetContactOfAnotherOwnerBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "105")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Питомца с таким ID не существует для get запроса у PetContact
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void getPetContactOfNonExistingPetBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "104")
                        .content(objectMapper.valueToTree(updatedPetContactDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        PetContact petContact = entityManager.find(PetContact.class, 104L);
        assertThat(petContact.getOwnerName()).isEqualTo("Максим");
        assertThat(petContact.getAddress()).isEqualTo("Липовая 3");
        assertThat(petContact.getDescription()).isEqualTo("Updated description");
        assertThat(petContact.getPhone()).isEqualTo(86967774242L);
        assertThat(petContact.getCode()).isEqualTo("FCED8B67FA84298BD8AB807D0E03774E");
    }

    // Питомец не пренадлежит текущему клиенту для update запроса у PetContact
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactOfAnotherOwnerBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "105")
                        .content(objectMapper.valueToTree(updatedPetContactDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Питомца с таким ID не существует для update запроса у PetContact
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactOfNonExistingPetBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "1000")
                        .content(objectMapper.valueToTree(updatedPetContactDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Не все поля были заполнены (1 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactWithEmptyDtoFirstFieldBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "104")
                        .content(objectMapper.valueToTree(petContactDtoWithEmptyFirstField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Не все поля были заполнены (2 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactWithEmptyDtoSecondFieldBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "104")
                        .content(objectMapper.valueToTree(petContactDtoWithEmptySecondField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Не все поля были заполнены (3 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactWithEmptyDtoThirdFieldBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "104")
                        .content(objectMapper.valueToTree(petContactDtoWithEmptyThirdField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Не все поля были заполнены (4 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void updatePetContactWithEmptyDtoFourthFieldBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "104")
                        .content(objectMapper.valueToTree(petContactDtoWithEmptyFourthField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void QrCodeSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/qr")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "107")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //Питомец не пренадлежит текущему клиенту для get запроса у QrCode
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void QrCodeOfAnotherOwnerBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/qr")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "105")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Питомца с таким ID не существует для get запроса у QrCode
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/petContactController/user-entities.yml",
            "/datasets/controllers/petContactController/pet-entities.yml",
            "/datasets/controllers/petContactController/pet-contact.yml"})
    public void QrCodeOfNonExistingPetBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/qr")
                        .header("Authorization", "Bearer " + token)
                        .param("petId", "1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
