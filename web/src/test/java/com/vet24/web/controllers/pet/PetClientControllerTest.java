package com.vet24.web.controllers.pet;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetDao;
import com.vet24.models.dto.pet.PetRequestPostDto;
import com.vet24.models.dto.pet.PetRequestPutDto;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class PetClientControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private PetDao petDao;

    private final String URI = "/api/client/pet";
    private PetRequestPostDto petRequestPostDto;

    private PetRequestPutDto petRequestDtoPut;
    private String token;


    @Before
    public void createNewClientAndDog() {

        this.petRequestPostDto = new PetRequestPostDto("name", "white", LocalDate.now(), PetType.DOG,
                "good", Gender.MALE, "white", PetSize.SMALL, 0.1, "test.png");

        this.petRequestDtoPut = new PetRequestPutDto("name", "white", LocalDate.now(),
                "good", Gender.MALE, "white", PetSize.SMALL, 0.1, "test.png");
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com","client");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void getAllPetsSuccess() throws Exception {
        mockMvc.perform(get(URI).header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, add pet - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml",
            "/datasets/pet-color.yml", "/datasets/pet-breed.yml"})
    public void persistPetSuccess() throws Exception {
        int sizeBefore = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, PetRequestPostDto.class)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petRequestPostDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(++sizeBefore).isEqualTo((petDao.getAll().size()));
    }

    // +mock, delete by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetSuccess() throws Exception {
        int sizeBefore = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}", 107)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petRequestPostDto).toString())
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
                        .content(objectMapper.valueToTree(petRequestPostDto).toString())
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
                        .content(objectMapper.valueToTree(petRequestPostDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // + mock, put pet by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml",
            "/datasets/pet-color.yml", "/datasets/pet-breed.yml"})
    public void updatePetSuccess() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 107)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petRequestDtoPut).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

    // +mock, put pet by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml",
            "/datasets/pet-color.yml", "/datasets/pet-breed.yml"})
    public void updatePetOfAnotherOwnerBadRequest() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petRequestDtoPut).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }

//    // +mock, put pet by id - error 400 type of pet can not be changed
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user_entities.yml", "/datasets/pet-entities.yml"})
//    public void updatePetChangeTypeBadRequest() throws Exception{
//        int beforeCount = petDao.getAll().size();
//        PetRequestDto.PutClass updatedCat
////        PetRequestDto updatedCat = new PetRequestDto();
//        updatedCat.setPetType(PetType.CAT);
//        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(updatedCat).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
//    }

    // +mock, put pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetThatDoesNotExistNotFound() throws Exception {
        int beforeCount = petDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}", 69000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(petRequestDtoPut).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(petDao.getAll().size());
    }
}