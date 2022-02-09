package com.vet24.web.controllers.pet;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetDao;
import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.CatDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Pet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PetControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private PetDao petDao;

    private final String URI = "/api/client/pet";
    private AbstractNewPetDto abstractNewPetDto;
    private String token;

    @Before
    public void createNewClientAndDog() {
        this.abstractNewPetDto = new DogDto("name", PetType.DOG, LocalDate.now(), Gender.MALE, "breed",
                "color", PetSize.MEDIUM, 9.3, "description", "test.png", 0);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com","client");
    }

    // +mock, add pet - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetSuccess() throws Exception {
        int sizeBefore = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/add", AbstractNewPetDto.class)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++sizeBefore).isEqualTo(petDao.getAll().size());
    }

    // +mock, delete by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetSuccess() throws Exception {
        int sizeBefore = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}", 107)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--sizeBefore).isEqualTo(petDao.getAll().size());
    }

    // +mock, delete pet by id - error pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetOfAnotherOwnerBadRequest() throws Exception {
        int sizeBefore = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(sizeBefore).isEqualTo(petDao.getAll().size());
    }

    // +mock, delete pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetThatDoesNotExistNotFound() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}", 69000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // + mock, put pet by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetSuccess() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 107)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // +mock, put pet by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetOfAnotherOwnerBadRequest() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // +mock, put pet by id - error 400 type of pet can not be changed
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetChangeTypeBadRequest() throws Exception{
        int beforeCount = petDao.getAll().size();
        AbstractNewPetDto abstractCat = new CatDto();
        abstractCat.setPetType(PetType.CAT);
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractCat).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // +mock, put pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetThatDoesNotExistNotFound() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 69000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // mock, put pet avatar
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getPetAvatarButPetDoesNotExistBadRequest() throws Exception {
        Pet pet = petDao.getByKey(69000L);
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/avatar", byte[].class, 69000L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(pet).isNull();
    }

    // +mock, pet avatar by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetAvatarSuccess() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
        mockMvc.perform(multipart(URI + "/{petId}/avatar", 107)
                        .file(mockMultipartFile)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "multipart/form-data"))
                .andExpect(status().isOk());
    }

    //+mock
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetAvatarOfAnotherOwnerBadRequest() throws Exception {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/avatar", String.class,  100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(abstractNewPetDto).toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //+mock
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetAvatarButPetDoesNotExistNotFound() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
        mockMvc.perform(multipart(URI + "/{petId}/avatar", 69000)
                        .file(mockMultipartFile)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "multipart/form-data"))
                .andExpect(status().isNotFound());
    }
}