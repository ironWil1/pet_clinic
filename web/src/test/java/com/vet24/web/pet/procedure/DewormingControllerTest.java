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

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DewormingControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    DewormingController dewormingController;

    @Autowired
    DewormingMapper dewormingMapper;

    @Autowired
    DewormingDao dewormingDao;

    final String URI = "/api/client/procedure";
    private String token;

    DewormingDto dewormingDto1;
    DewormingDto dewormingDto2;

    @Before
    public void initDto() {
        this.dewormingDto1 = new DewormingDto(LocalDate.now(), 100L, "4f435", false, 5);
        this.dewormingDto2 = dewormingMapper.toDto(dewormingDao.getByKey(102L));
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    // +mock, GET deworming by id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testGetDewormingByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dewormingDto2.getId().intValue())))
                .andExpect(jsonPath("$.date", is(dewormingDto2.getDate().toString())))
                .andExpect(jsonPath("$.medicineId", is(dewormingDto2.getMedicineId().intValue())))
                .andExpect(jsonPath("$.medicineBatchNumber", is(dewormingDto2.getMedicineBatchNumber())))
                .andExpect(jsonPath("$.isPeriodical", is(dewormingDto2.getIsPeriodical())))
                .andExpect(jsonPath("$.periodDays", is(dewormingDto2.getPeriodDays())));
    }


    // +mock, GET deworming by id - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testGetDewormingByIdErrorDewormingNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET deworming by id - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testGetDewormingByIdErrorDewormingNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET dewormings by pet id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testGetDewormingsByPetIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dewormingDto2.getId().intValue())))
                .andExpect(jsonPath("$[0].date", is(dewormingDto2.getDate().toString())))
                .andExpect(jsonPath("$[0].medicineId", is(dewormingDto2.getMedicineId().intValue())))
                .andExpect(jsonPath("$[0].medicineBatchNumber", is(dewormingDto2.getMedicineBatchNumber())))
                .andExpect(jsonPath("$[0].isPeriodical", is(dewormingDto2.getIsPeriodical())))
                .andExpect(jsonPath("$[0].periodDays", is(dewormingDto2.getPeriodDays())));
    }

    // +mock, GET dewormings by pet id - 400 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testGetDewormingsByPetIdErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET dewormings by pet id - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testGetDewormingsByPetIdErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, ADD new deworming - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testAddDewormingSuccess() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.date", is(dewormingDto1.getDate().toString())))
                .andExpect(jsonPath("$.medicineId", is(dewormingDto1.getMedicineId().intValue())))
                .andExpect(jsonPath("$.medicineBatchNumber", is(dewormingDto1.getMedicineBatchNumber())))
                .andExpect(jsonPath("$.isPeriodical", is(dewormingDto1.getIsPeriodical())))
                .andExpect(jsonPath("$.periodDays", is(dewormingDto1.getPeriodDays())));
        assertThat(++beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, ADD new deworming - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testAddDewormingErrorPetNotFound() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, ADD new deworming - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testAddDewormingErrorPetNotYours() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, UPDATE  deworming - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testUpdateDewormingSuccess() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dewormingDto2.getId().intValue())))
                .andExpect(jsonPath("$.date", is(dewormingDto1.getDate().toString())))
                .andExpect(jsonPath("$.medicineId", is(dewormingDto1.getMedicineId().intValue())))
                .andExpect(jsonPath("$.medicineBatchNumber", is(dewormingDto1.getMedicineBatchNumber())))
                .andExpect(jsonPath("$.isPeriodical", is(dewormingDto1.getIsPeriodical())))
                .andExpect(jsonPath("$.periodDays", is(dewormingDto1.getPeriodDays())));
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }


    // +mock, UPDATE  deworming - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testUpdateDewormingErrorDewormingNotFound() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, UPDATE  deworming - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testUpdateDewormingErrorDewormingNotYours() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dewormingDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, DELETE deworming - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testDeleteDewormingSuccess() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertThat(--beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, DELETE deworming - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testDeleteDewormingErrorDewormingNotFound() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

    // +mock, DELETE deworming - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/dewormingController.yml", "datasets/reproduction.yml"})
    public void testDeleteDewormingErrorDewormingNotYours() throws Exception {
        int beforeCount = dewormingDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        assertThat(beforeCount).isEqualTo(dewormingDao.getAll().size());
    }

}
