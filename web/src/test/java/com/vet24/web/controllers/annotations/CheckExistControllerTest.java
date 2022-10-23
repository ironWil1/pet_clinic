package com.vet24.web.controllers.annotations;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.exception.BadRequestException;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.junit.Assert.assertEquals;



public class CheckExistControllerTest extends ControllerAbstractIntegrationTest {


    final String URI = "/api/doctors";
    private String token;

    @Before
    public void setToken() throws Exception {

        token = getAccessToken("doctor103@email.com", "doctor");
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/checkExistControllerTest/user-entities.yml",
            "/datasets/controllers/checkExistControllerTest/pet-entities.yml",
            "/datasets/controllers/checkExistControllerTest/clinical-examination.yml"})
    public void testGetExaminationByPetIdSuccessBecauseIdExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/exam")
                        .param("petId", "100")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/checkExistControllerTest/user-entities.yml",
            "/datasets/controllers/checkExistControllerTest/pet-entities.yml",
            "/datasets/controllers/checkExistControllerTest/clinical-examination.yml"})
    public void testGetExaminationByPetIdAndIdNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/exam")
                        .param("petId", "77")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull
                        (result.getResolvedException()).getClass(), BadRequestException.class));

    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/checkExistControllerTest/user-entities.yml",
            "/datasets/controllers/checkExistControllerTest/pet-entities.yml",
            "/datasets/controllers/checkExistControllerTest/clinical-examination.yml"})
    public void testGetExaminationByDataAndPetIdSuccessBecauseIdExist() throws Exception {
        String data = "2020-01-01";
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/exams")
                        .param(data, "2020-01-01")
                        .param("petId", "102")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

        @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/checkExistControllerTest/user-entities.yml",
            "/datasets/controllers/checkExistControllerTest/pet-entities.yml",
            "/datasets/controllers/checkExistControllerTest/clinical-examination.yml"})
    public void testGetExaminationByDataAndPetIdAndIdNotExist() throws Exception {
        String data = "2022-01-01";
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/exam")
                        .param(data, "2022-01-01")
                        .param("petId", "99")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull
                        (result.getResolvedException()).getClass(), BadRequestException.class));

    }
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/checkExistControllerTest/user-entities.yml",
            "/datasets/controllers/checkExistControllerTest/pet-entities.yml",
            "/datasets/controllers/checkExistControllerTest/clinical-examination.yml"})
    public void testGetExaminationByTwoIdsSuccessBecauseIdsExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/103/exam")
                        .param("petId", "104")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/checkExistControllerTest/user-entities.yml",
            "/datasets/controllers/checkExistControllerTest/pet-entities.yml",
            "/datasets/controllers/checkExistControllerTest/clinical-examination.yml"})
    public void testGetExaminationByTwoIdsAndIdsNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/103/exam")
                        .param("petId", "88")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull
                        (result.getResolvedException()).getClass(), BadRequestException.class));

    }
}
