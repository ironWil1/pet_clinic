package com.vet24.web.pet.clinicalexamination;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.clinicalexamination.ClinicalExaminationDao;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationMapper;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.clinicalexamination.ClinicalExaminationController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClinicalExaminationControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    ClinicalExaminationController clinicalExaminationController;

    @Autowired
    ClinicalExaminationDao clinicalExaminationDao;

    @Autowired
    ClinicalExaminationMapper clinicalExaminationMapper;

    final String URI = "/api/doctor/exam";
    final HttpHeaders HEADERS = new HttpHeaders();
    private String token;
    ClinicalExaminationDto clinicalExaminationDtoNew1;
    ClinicalExaminationDto clinicalExaminationDtoNew2;
    ClinicalExaminationDto clinicalExaminationDto1;
    ClinicalExaminationDto clinicalExaminationDto2;
    ClinicalExaminationDto clinicalExaminationDto3;
    ClinicalExaminationDto clinicalExaminationDto4;
    ClinicalExaminationDto clinicalExaminationDto5;

    @Before
    public void createNewClinicalExaminationAndClinicalExaminationDto() {
        this.clinicalExaminationDtoNew1 = new ClinicalExaminationDto(100L, 100L, 10.0, true, "textNew");
        this.clinicalExaminationDtoNew2 = new ClinicalExaminationDto(100L, 107670L, 10.0, true, "textNew");
        this.clinicalExaminationDto1 = new ClinicalExaminationDto(101L, 101L, 20.0, true, "text1");
        this.clinicalExaminationDto2 = new ClinicalExaminationDto(1545452L, 102L, 40.0, true, "text3");
        this.clinicalExaminationDto3 = new ClinicalExaminationDto(102L, 102L, 40.0, true, "text3");
        this.clinicalExaminationDto4 = new ClinicalExaminationDto(101L, 1043432L, 40.0, true, "text3");
        this.clinicalExaminationDto5 = new ClinicalExaminationDto(101L, 106L, 40.0, true, "text3");
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("doctor103@email.com","doctor");
    }

    // +mock, get clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{examinationId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, get ClinicalExamination by id - ClinicalExamination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationErrorClinicalExaminationNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{examinationId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, add ClinicalExamination - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationSuccess() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, add ClinicalExamination - pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationErrorPetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/reproduction", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // +mock, put clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationSuccess() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examinationId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, put clinical examination by id - PetNotAssigned
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorPetNotAssigned() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examinationId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, put clinical examination by id - BadRequest
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorBadRequest() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examinationId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, put clinical examination by id - pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorPetNotFound() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examinationId}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto4).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, put clinical examination by id - Not Found clinical examination
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorClinicalExaminationNotFound() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{examinationId}", 108562)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, delete clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationSuccess() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{examinationId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }

    // +mock, delete clinical examination by id - clinical examination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationErrorNotFound() throws Exception {
        int beforeCount = clinicalExaminationDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{examinationId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(clinicalExaminationDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(clinicalExaminationDao.getAll().size());
    }
}
