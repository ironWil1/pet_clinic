package com.vet24.web.pet.reproduction;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.reproduction.ReproductionDao;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.service.pet.reproduction.ReproductionService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.reproduction.ReproductionController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WithUserDetails(value = "user3@gmail.com")
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

    final String URI = "http://localhost:8090/api/client/pet";

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


    // +mock, get reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 102, 102))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, get reproduction by id -  error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void shouldBeNotFoundPet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 33, 102))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, reproduction by id -  error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError404reproduction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 102, 33))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError400refPetReproduction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 101, 102))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, get reproduction by id -  error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError400refClientPet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/reproduction/{id}", 100, 100))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, add reproduction - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 102)
                .content(objectMapper.valueToTree(reproductionDtoNew).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, add reproduction - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionError404() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 33)
                .content(objectMapper.valueToTree(reproductionDtoNew).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // + mock, add reproduction - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionError400() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 100)
                .content(objectMapper.valueToTree(reproductionDtoNew).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // + mock, put reproduction by id - success //URI + "/{petId}/reproduction/{id}" 102,102
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 102, 102)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError404reproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 33, 102)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError404pet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 102, 33)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400refPetReproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 101, 102)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 400 reproductionId in path and in body not equals
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400idInPathAndBody() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 102, 102)
                .content(objectMapper.valueToTree(reproductionDto1).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, put reproduction by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400refClientPet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/reproduction/{id}", 100, 100)
                .content(objectMapper.valueToTree(reproductionDto1).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 102, 102)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError404reproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 33, 102)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError404pet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 102, 33)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError400refPetReproduction() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 101, 102)
                .content(objectMapper.valueToTree(reproductionDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }

    // +mock, delete reproduction by id - error pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError400refClientPet() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/reproduction/{id}", 100, 100)
                .content(objectMapper.valueToTree(reproductionDto1).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(reproductionDao.getAll().size());
    }
}
