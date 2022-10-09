package com.vet24.web.pet.procedure;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.mappers.pet.procedure.ExternalParasiteMapper;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
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

public class ExternalParasiteControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    ExternalParasiteMapper externalParasiteMapper;

    private static final String URI = "/api/client/procedure";
    private static final ExternalParasiteDto DTO_FOR_JSON = new ExternalParasiteDto(LocalDate.now(), 100L,
            "4f435", false, 5);
    private static final long INITIAL_SIZE = 4;
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user2@gmail.com", "user2");
    }

    private ExternalParasiteDto getById(Long id) {
        return externalParasiteMapper.toDto(entityManager.find(ExternalParasiteProcedure.class, id));
    }

    private long getSize() {
        return entityManager.createQuery("SELECT COUNT(e) FROM ExternalParasiteProcedure e", Long.class)
                .getSingleResult();
    }

    private ResultMatcher expectJsonObject(ExternalParasiteDto externalParasiteDto) {
        return ResultMatcher.matchAll(
                jsonPath("$.date", is(externalParasiteDto.getDate().toString())),
                jsonPath("$.medicineId", is(externalParasiteDto.getMedicineId().intValue())),
                jsonPath("$.medicineBatchNumber", is(externalParasiteDto.getMedicineBatchNumber())),
                jsonPath("$.isPeriodical", is(externalParasiteDto.getIsPeriodical())),
                jsonPath("$.periodDays", is(externalParasiteDto.getPeriodDays()))
        );
    }

    private ResultMatcher expectJsonArray(String prefix, ExternalParasiteDto externalParasiteDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id", hasItem(externalParasiteDto.getId().intValue())),
                jsonPath(prefix + ".date", hasItem(externalParasiteDto.getDate().toString())),
                jsonPath(prefix + ".medicineId", hasItem(externalParasiteDto.getMedicineId().intValue())),
                jsonPath(prefix + ".medicineBatchNumber", hasItem(externalParasiteDto.getMedicineBatchNumber())),
                jsonPath(prefix + ".isPeriodical", hasItem(externalParasiteDto.getIsPeriodical())),
                jsonPath(prefix + ".periodDays", hasItem(externalParasiteDto.getPeriodDays()))
        );
    }

    // +mock, GET by id - 200 SUCCESS "Успешно получены все записи по обработку от эктопаразитов"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testGetExternalParasiteByIdSuccess() throws Exception {
        ExternalParasiteDto externalParasiteDto = getById(102L);
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/external/{id}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(102)))
                .andExpect(expectJsonObject(externalParasiteDto));
    }

    // +mock, GET by id - 404 ERROR "Процедура не найдена"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testGetExternalParasiteByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/external/{id}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // +mock, GET by id - 400 ERROR "Данный питомец Вам не принадлежит"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testGetExternalParasiteByIdNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/external/{id}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, GET by pet id - 200 SUCCESS "Успешно найдена запись на обработку от эктопаразитов"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testGetExternalParasiteByPetIdSuccess() throws Exception {
        ExternalParasiteDto externalParasiteDto = getById(102L);
        ExternalParasiteDto externalParasiteDto1 = getById(103L);
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/external?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(expectJsonArray("$[?(@.id == 102)]", externalParasiteDto))
                .andExpect(expectJsonArray("$[?(@.id == 103)]", externalParasiteDto1));
    }

    // +mock, GET by pet id - 404 ERROR "Питомец не найден"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testGetExternalParasiteByPetIdPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/external?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // +mock, GET by pet id - 400 ERROR "Данный питомец Вам не принадлежит"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testGetExternalParasiteByPetIdPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/external?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // +mock, ADD - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testAddExternalParasiteSuccess() throws Exception {
        long beforeCount = INITIAL_SIZE;
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/external?petId={petId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(expectJsonObject(DTO_FOR_JSON));
        assertThat(++beforeCount).isEqualTo(getSize());
    }

    // +mock, ADD - 404 ERROR "Питомец не найден"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testAddExternalParasitePetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/external?petId={petId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, ADD - 400 ERROR "Данный питомец Вам не принадлежит"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testAddExternalParasiteErrorNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/external?petId={petId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, UPDATE - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testUpdateExternalParasiteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/external/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(expectJsonObject(DTO_FOR_JSON));
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

//    mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 101)
//            .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.writeValueAsString(updateVaccinationDto))
//            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(jsonObjectValidate(updateVaccinationDto));
//    assertThat(2L).isEqualTo(getVaccinationListSize(TEST_PET_ID));

    // +mock, UPDATE - 404 ERROR "Процедура не найдена"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testUpdateExternalParasiteErrorNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/external/{id}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, UPDATE - 400 ERROR "Данная процедура другого клиента"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testUpdateExternalParasiteNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/external/{id}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(DTO_FOR_JSON).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, DELETE - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testDeleteExternalParasiteSuccess() throws Exception {
        long beforeCount = INITIAL_SIZE;
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/external/{id}", 102)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertThat(--beforeCount).isEqualTo(getSize());
    }

    // +mock, DELETE - 404 ERROR "Процедура не найдена"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testDeleteExternalParasiteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/external/{id}", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }

    // +mock, DELETE - 400 ERROR "Данная процедура другого клиента"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/externalParasiteControllerTest/users.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/pets.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/medicines.yml",
            "/datasets/controllers/pet/procedure/externalParasiteControllerTest/externalParasiteProcedures.yml"})
    public void testDeleteExternalParasiteNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/external/{id}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
        assertThat(INITIAL_SIZE).isEqualTo(getSize());
    }
}
