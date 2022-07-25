package com.vet24.web.pet.procedure;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.procedure.DewormingDto;
import com.vet24.models.mappers.pet.procedure.DewormingMapper;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DewormingControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    DewormingMapper dewormingMapper;

    private static final String URI = "/api/client/procedure";
    private static final DewormingDto DTO_FOR_JSON = new DewormingDto(LocalDate.now(), 100L, "4f435", false, 5);
    private static final long INITIAL_SIZE = 4;
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user2@gmail.com", "user2");
    }

    private DewormingDto getById(Long id) {
        return dewormingMapper.toDto(entityManager.find(Deworming.class, id));
    }

    private long getSize() {
        return entityManager.createQuery("SELECT COUNT(d) FROM Deworming d", Long.class).getSingleResult();
    }

    private ResultMatcher expectJsonObject(DewormingDto dewormingDto) {
        return ResultMatcher.matchAll(
                jsonPath("$.date", is(dewormingDto.getDate().toString())),
                jsonPath("$.medicineId", is(dewormingDto.getMedicineId().intValue())),
                jsonPath("$.medicineBatchNumber", is(dewormingDto.getMedicineBatchNumber())),
                jsonPath("$.isPeriodical", is(dewormingDto.getIsPeriodical())),
                jsonPath("$.periodDays", is(dewormingDto.getPeriodDays()))
        );
    }

    private ResultMatcher expectJsonArray(String prefix, DewormingDto dewormingDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id", hasItem(dewormingDto.getId().intValue())),
                jsonPath(prefix + ".date", hasItem(dewormingDto.getDate().toString())),
                jsonPath(prefix + ".medicineId", hasItem(dewormingDto.getMedicineId().intValue())),
                jsonPath(prefix + ".medicineBatchNumber", hasItem(dewormingDto.getMedicineBatchNumber())),
                jsonPath(prefix + ".isPeriodical", hasItem(dewormingDto.getIsPeriodical())),
                jsonPath(prefix + ".periodDays", hasItem(dewormingDto.getPeriodDays()))
        );
    }

    // +mock, GET deworming by id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testGetDewormingByIdSuccess() throws Exception {
        DewormingDto dewormingDto = getById(102L);
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(102)))
                .andExpect(expectJsonObject(dewormingDto));
    }

    // +mock, GET deworming by id - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testGetDewormingByIdErrorDewormingNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET deworming by id - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testGetDewormingByIdErrorDewormingNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET dewormings by pet id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testGetDewormingsByPetIdSuccess() throws Exception {
        DewormingDto dewormingDto1 = getById(102L);
        DewormingDto dewormingDto2 = getById(103L);
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(expectJsonArray("$[?(@.id == 102)]", dewormingDto1))
                .andExpect(expectJsonArray("$[?(@.id == 103)]", dewormingDto2));
    }

    // +mock, GET dewormings by pet id - 400 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testGetDewormingsByPetIdErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET dewormings by pet id - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testGetDewormingsByPetIdErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/deworming?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, ADD new deworming - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testAddDewormingSuccess() throws Exception {
        long beforeCount = INITIAL_SIZE;
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(expectJsonObject(DTO_FOR_JSON));
        assertThat(++beforeCount).isEqualTo(getSize());
    }

    // +mock, ADD new deworming - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testAddDewormingErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, ADD new deworming - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testAddDewormingErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/deworming?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, UPDATE  deworming - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testUpdateDewormingSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(102)))
                .andExpect(expectJsonObject(DTO_FOR_JSON));
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, UPDATE  deworming - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testUpdateDewormingErrorDewormingNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, UPDATE  deworming - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testUpdateDewormingErrorDewormingNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, DELETE deworming - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testDeleteDewormingSuccess() throws Exception {
        long beforeCount = INITIAL_SIZE;
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertThat(--beforeCount).isEqualTo(getSize());
    }

    // +mock, DELETE deworming - 400 ERROR "deworming not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testDeleteDewormingErrorDewormingNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, DELETE deworming - 400 ERROR "deworming not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/dewormingController/user.yml", "/datasets/dewormingController/pet.yml", "/datasets/dewormingController/medicine.yml", "/datasets/dewormingController/deworming.yml"})
    public void testDeleteDewormingErrorDewormingNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/deworming/{dewormingId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }
}
