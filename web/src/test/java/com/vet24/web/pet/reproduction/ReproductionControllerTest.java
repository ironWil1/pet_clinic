package com.vet24.web.pet.reproduction;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.reproduction.ReproductionDao;
import com.vet24.models.dto.pet.procedure.DewormingDto;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.service.pet.reproduction.ReproductionService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.reproduction.ReproductionController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ReproductionControllerTest extends ControllerAbstractIntegrationTest {
    @Autowired
    ReproductionController reproductionController;
    @Autowired
    ReproductionMapper reproductionMapper;
    @Autowired
    ReproductionDao reproductionDao;
    @Autowired
    PetDao petDao;
    @Autowired
    ReproductionService reproductionService;

    final String URI = "/api/client/pet";
    private String token;

    ReproductionDto reproductionDtoNew;
    ReproductionDto reproductionDto1;
    ReproductionDto reproductionDto3;

    // client 4 --> pet 1 --> reproduction 1 (to check client-pet link)
    // client 3 --> pet 2 --> reproduction 2 (to check pet-reproduction link)
    //        `---> pet 3 --> reproduction 3 (to get & update & delete)
    //                  `---> reproduction 4 (to create)

    @Before
    public void createNewReproductionAndReproductionDto() {
        this.reproductionDtoNew = new ReproductionDto(4L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 4);
        this.reproductionDto1 = new ReproductionDto(100L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 11);
        this.reproductionDto3 = new ReproductionDto(102L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 33);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    private ReproductionDto getById(Long id) {
        return reproductionMapper.toDto(entityManager.find(Reproduction.class, id));
    }

    private ResultMatcher expectJsonArray(Integer indexArr, ReproductionDto reproductionDto) {
        return ResultMatcher.matchAll(
                jsonPath("$[" + indexArr + "].childCount", is(reproductionDto.getChildCount())),
                jsonPath("$[" + indexArr + "].estrusStart", is(reproductionDto.getEstrusStart().toString())),
                jsonPath("$[" + indexArr + "].mating", is(reproductionDto.getMating().toString())),
                jsonPath("$[" + indexArr + "].dueDate", is(reproductionDto.getDueDate().toString()))
        );
    }

    //Получить все репродукции по petId - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/reproductionController/user-entities.yml",
            "/datasets/controllers/reproductionController/pet-entities.yml",
            "/datasets/controllers/reproductionController/reproduction.yml"})
    public void testGetAllReproductionSuccess() throws Exception {
        ReproductionDto reproductionDto102 = getById(102L);
        ReproductionDto reproductionDto104 = getById(104L);
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id", is(102)))
                .andExpect(jsonPath("$[1].id", is(104)))
                .andExpect(expectJsonArray(0, reproductionDto102))
                .andExpect(expectJsonArray(1, reproductionDto104));
    }

    // Получить все репродукции по petId - 404 Pet not found
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/reproductionController/user-entities.yml",
            "/datasets/controllers/reproductionController/pet-entities.yml",
            "/datasets/controllers/reproductionController/reproduction.yml"})
    public void testGetAllReproductionErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Получить все репродукции по petId - 400 Питомец вам не принадлежит
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/reproductionController/user-entities.yml",
            "/datasets/controllers/reproductionController/pet-entities.yml",
            "/datasets/controllers/reproductionController/reproduction.yml"})
    public void testGetAllReproductionErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Получить все репродукции по petId - 400 PetId не указан
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/reproductionController/user-entities.yml",
            "/datasets/controllers/reproductionController/pet-entities.yml",
            "/datasets/controllers/reproductionController/reproduction.yml"})
    public void testGetAllReproductionErrorNoPetId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/reproduction")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, get reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 102, 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, get reproduction by id -  error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void shouldBeNotFoundPet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 33, 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, reproduction by id -  error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError404reproduction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 102, 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError400refPetReproduction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 101, 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, get reproduction by id -  error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError400refClientPet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 100, 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, add reproduction - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, add reproduction - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionError404() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // + mock, add reproduction - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionError400() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // + mock, put reproduction by id - success //URI + "/{petId}/reproduction/{id}" 102,102
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 102, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError404reproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 33, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError404pet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 102, 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400refPetReproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 101, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400refClientPet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 100, 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 102, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError404reproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 33, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError404pet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 102, 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError400refPetReproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 101, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError400refClientPet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 100, 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(reproductionDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }
}
