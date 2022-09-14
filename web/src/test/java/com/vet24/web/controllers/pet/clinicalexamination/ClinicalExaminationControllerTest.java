package com.vet24.web.controllers.pet.clinicalexamination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationRequestDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationResponseDto;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.web.ControllerAbstractIntegrationTest;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

public class ClinicalExaminationControllerTest extends ControllerAbstractIntegrationTest {

    final String URI = "/api/doctor/exam";
    final HttpHeaders HEADERS = new HttpHeaders();
    private String token;

    private ClinicalExaminationRequestDto clinicalExaminationRequestDto;
    private ClinicalExaminationResponseDto clinicalExaminationResponseDto;
    private ClinicalExaminationRequestDto clinicalExaminationRequestDto1;
    private ClinicalExaminationRequestDto clinicalExaminationRequestDto2;


    @Before
    public void createNewClinicalExaminationAndClinicalExaminationResponseDto() {
        this.clinicalExaminationRequestDto = new ClinicalExaminationRequestDto(10.0, true, "textNew");
        this.clinicalExaminationResponseDto = new ClinicalExaminationResponseDto(102L, 102L, 40.0, true, "text3");
        this.clinicalExaminationRequestDto1 = new ClinicalExaminationRequestDto(20.0, true, "text1");
        this.clinicalExaminationRequestDto2 = new ClinicalExaminationRequestDto(40.0, true, "text3");
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("doctor103@email.com", "doctor");
    }

    //Список клинических обследований питомца найден - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testGetByPetIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                                .param("petId", "100")
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationResponseDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].petId").value(100))
                .andExpect(jsonPath("$[0].weight").value(100))
                .andExpect(jsonPath("$[0].isCanMove").value(true))
                .andExpect(jsonPath("$[0].text").value("text1"));
    }

    //Указан неверный ID питомца - 400 BAD_REQUEST "no pet with this id"
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testGetByPetIdBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                                .param("petId", "-100")
                                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Клинические обследования с таким ID питомца не найдены - 404 NOT_FOUND "clinical examination not found"
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testGetByPetIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                                .param("petId", "103")
                                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, get clinical examination by id - 200 success
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testGetClinicalExaminationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{examId}", 102)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationResponseDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(102)))
                .andExpect(jsonPath("$.petId", Is.is(102)))
                .andExpect(jsonPath("$.weight", Is.is(102.0)))
                .andExpect(jsonPath("$.isCanMove", Is.is(true)))
                .andExpect(jsonPath("$.text", Is.is("text3")));
    }

    // +mock, get ClinicalExamination by id - 404 ClinicalExamination not found
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testGetClinicalExaminationErrorClinicalExaminationNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{examId}", 33)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationResponseDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("clinical examination not found")));
    }

    // +mock, add ClinicalExamination - 200 success
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testAddClinicalExaminationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                                .param("petId", "102")
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.weight", Is.is(10.0)))
                .andExpect(jsonPath("$.isCanMove", Is.is(true)))
                .andExpect(jsonPath("$.text", Is.is("textNew")));

        assertTrue("Проверка наличия новой информации о диспансеризации в БД", entityManager.createQuery(
                        "SELECT COUNT(id) > 0 FROM ClinicalExamination WHERE " +
                                "weight = :weight AND isCanMove = :isCanMove AND text = :text", Boolean.class)
                .setParameter("weight", clinicalExaminationRequestDto.getWeight())
                .setParameter("isCanMove", clinicalExaminationRequestDto.getIsCanMove())
                .setParameter("text", clinicalExaminationRequestDto.getText())
                .getSingleResult());
    }

    // +mock, add ClinicalExamination - 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testAddClinicalExaminationErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                                .param("petId", "33")
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationRequestDto1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, put clinical examination by id - 200 success
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testPutClinicalExaminationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examId}", 102)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationRequestDto1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        ClinicalExamination clinicalExamination =
                entityManager.createQuery("SELECT c from ClinicalExamination c WHERE c.id = 102",
                                          ClinicalExamination.class).getSingleResult();
        assertEquals(Optional.of(20.0), Optional.of(clinicalExamination.getWeight()));
        Pet pet = entityManager.createQuery(
                "SELECT p FROM Pet p JOIN ClinicalExamination ce ON ce.pet.id = p.id WHERE ce.id = 102",
                Pet.class).getSingleResult();
        assertEquals(Optional.of(20.0), Optional.of(pet.getWeight()));
    }

    // +mock, put clinical examination by id - 404 Not Found clinical examination
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorClinicalExaminationNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examId}", 108562)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(clinicalExaminationRequestDto2))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("clinical examination not found")));
    }

    // +mock, delete clinical examination by id - 200 success
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testDeleteClinicalExaminationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{examId}", 100)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse("Проверка удаления информации о диспансеризации из БД",
                    entityManager.createQuery("SELECT COUNT(c) > 0 FROM ClinicalExamination c WHERE c.id = 100",
                                              Boolean.class).getSingleResult());
    }

    // +mock, delete clinical examination by id - 404 clinical examination not found
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/clinicalExaminationController/user-entities.yml",
            "/datasets/controllers/clinicalExaminationController/pet-entities.yml",
            "/datasets/controllers/clinicalExaminationController/clinical-examination.yml"})
    public void testDeleteClinicalExaminationErrorNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{examId}", 33)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("clinical examination not found")));
        assertEquals(Long.valueOf(5),
                     entityManager.createQuery("SELECT COUNT(c) FROM ClinicalExamination c", Long.class)
                             .getSingleResult());
    }
}
