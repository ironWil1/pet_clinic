package com.vet24.web.controllers.pet.procedure;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.mappers.pet.procedure.VaccinationMapper;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class VaccinationControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    VaccinationMapper vaccinationMapper;
    final String URI = "/api/client/procedure/vaccination";
    private String token;
    private final long TEST_PET_ID = 101;
    private VaccinationDto newVaccinationDto;
    private VaccinationDto updateVaccinationDto;
    private VaccinationDto vaccinationDto101;
    private VaccinationDto vaccinationDto102;

    @Before
    public void createModels() {
        this.newVaccinationDto = new VaccinationDto(LocalDate.now(), 100L, "4f435", false, null);
        this.updateVaccinationDto = new VaccinationDto(LocalDate.now(), 100L, "4f435", true, 2);
        vaccinationDto101 = vaccinationMapper.toDto(entityManager.find(VaccinationProcedure.class, 101L));
        vaccinationDto102 = vaccinationMapper.toDto(entityManager.find(VaccinationProcedure.class, 102L));
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    public ResultMatcher jsonObjectValidate(VaccinationDto vaccinationDto) {
        return ResultMatcher.matchAll(
                jsonPath("$.date", is(vaccinationDto.getDate().toString())),
                jsonPath("$.medicineId", is(vaccinationDto.getMedicineId().intValue())),
                jsonPath("$.medicineBatchNumber", is(vaccinationDto.getMedicineBatchNumber())),
                jsonPath("$.isPeriodical", is(vaccinationDto.getIsPeriodical())),
                jsonPath("$.periodDays", is(vaccinationDto.getPeriodDays()))
        );
    }

    public ResultMatcher jsonArrayValidate(Integer arrayIndex, VaccinationDto vaccinationDto) {
        return ResultMatcher.matchAll(

                jsonPath("$[" + arrayIndex + "].date", is(vaccinationDto.getDate().toString())),
                jsonPath("$[" + arrayIndex + "].medicineId", is(vaccinationDto.getMedicineId().intValue())),
                jsonPath("$[" + arrayIndex + "].medicineBatchNumber", is(vaccinationDto.getMedicineBatchNumber())),
                jsonPath("$[" + arrayIndex + "].isPeriodical", is(vaccinationDto.getIsPeriodical())),
                jsonPath("$[" + arrayIndex + "].periodDays", is(vaccinationDto.getPeriodDays()))
        );
    }

    public Long getVaccinationListSize(Long petId) {
        return entityManager.createQuery("select count(v) from VaccinationProcedure v where pet_id = :pet_id",Long.class)
                .setParameter("pet_id", petId).getSingleResult();
    }


    //Получить все процедуры вакцинации по petId - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testGetVaccinationListSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .param("petId", "101")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id", is(101)))
                .andExpect(jsonPath("$[1].id", is(102)))
                .andExpect(jsonArrayValidate(0, vaccinationDto101))
                .andExpect(jsonArrayValidate(1, vaccinationDto102));
    }

    // Получить все процедуры вакцинации по petId - 404 Pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testGetVaccinationListErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .param("petId", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Получить все процедуры вакцинации по petId - 404 Vaccination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testGetVaccinationListErrorVaccinationNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .param("petId", "106")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Получить процедуру вакцинации по ID - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testGetVaccinationByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", TEST_PET_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonObjectValidate(vaccinationDto101));
    }

    // Получить процедуру вакцинации по ID  - 404 Pet or Procedure not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testGetVaccinationByIdErrorProcedureNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //Получить процедуру вакцинации по ID  - 400 ERROR Pet not assigned with Procedure OR pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testGetVaccinationByIdErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Добавить процедуру вакцинации - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testAddVaccinationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .param("petId", "101")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(newVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonObjectValidate(newVaccinationDto));
        assertThat(3L).isEqualTo(getVaccinationListSize(TEST_PET_ID));
    }

    //Добавить процедуру вакцинации - 404 Pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testAddVaccinationErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .param("petId", "1")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(newVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //Добавить процедуру вакцинации - 400 Pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testAddVaccinationErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .param("petId", "100")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(newVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Обновить процедуру вакцинации - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testUpdateVaccinationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonObjectValidate(updateVaccinationDto));
        assertThat(2L).isEqualTo(getVaccinationListSize(TEST_PET_ID));
    }

    //Обновить процедуру вакцинации - 404 Pet or Procedure not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testUpdateVaccinationErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 1)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //Обновить процедуру вакцинации - 400 Pet not assigned with Procedure OR pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testUpdateVaccinationErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Удалить процедуру вакцинации - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testDeleteVaccinationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", TEST_PET_ID)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(newVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(1L).isEqualTo(getVaccinationListSize(TEST_PET_ID));
    }

    //Удалить процедуру вакцинации - 404 Pet or Procedure not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testDeleteVaccinationErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(newVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //Удалить процедуру вакцинации - 400 Pet not assigned with Procedure OR pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/pet/procedure/vaccination_controller/user-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/pet-entities.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/medicine.yml",
            "/datasets/controllers/pet/procedure/vaccination_controller/vaccination.yml",
            "datasets/controllers/pet/procedure/vaccination_controller/reproduction.yml"})
    public void testDeleteVaccinationErrorPetNotYours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(newVaccinationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

