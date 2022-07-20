package com.vet24.web.pet.procedure;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.procedure.DewormingDao;
import com.vet24.models.dto.pet.procedure.DewormingDto;
import com.vet24.models.mappers.pet.procedure.DewormingMapper;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.procedure.DewormingController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DewormingControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    DewormingController dewormingController;

    @Autowired
    DewormingMapper dewormingMapper;

    @Autowired
    DewormingDao dewormingDao;

    final String URI = "/api/client/procedure";
    private String token;

    DewormingDto dewormingDto;

    // client 4 --> pet 1 --> procedure 1 (to check client-pet link)
    // client 3 --> pet 2 --> procedure 2 (to check pet-procedure link)
    //        `---> pet 3 --> procedure 3 (to get & update & delete)
    //                  `---> procedure 4 (to create)

    @Before
    public void createModels() {
        this.dewormingDto = new DewormingDto(LocalDate.now(), 100L, "4f435", false, 5);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com","user3");
    }

    // +mock, GET deworming by id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testGetDewormingByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    // +mock, GET deworming by id - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testGetDewormingByIdErrorDewormingNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, GET deworming by id - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testGetDewormingByIdErrorDewormingNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, GET dewormings by pet id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testGetDewormingsByPetIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, GET dewormings by pet id - 400 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testGetDewormingsByPetIdErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, GET dewormings by pet id - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testGetDewormingsByPetIdErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // +mock, ADD new deworming - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testAddDewormingSuccess() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, ADD new deworming - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testAddDewormingErrorPetNotFound() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, ADD new deworming - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testAddDewormingErrorPetNotYours() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, UPDATE  deworming - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testUpdateDewormingSuccess() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }


    // +mock, UPDATE  deworming - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testUpdateDewormingErrorDewormingNotFound() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, UPDATE  deworming - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testUpdateDewormingErrorDewormingNotYours() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, DELETE deworming - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testDeleteDewormingSuccess() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, DELETE deworming - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testDeleteDewormingErrorDewormingNotFound() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, DELETE deworming - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/deworming.yml", "datasets/reproduction.yml"})
    public void testDeleteDewormingErrorDewormingNotYours() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

}
