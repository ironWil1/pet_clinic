package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.medicine.DiagnosisDao;
import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.service.user.DoctorService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DoctorControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    DoctorService doctorService;
    @Autowired
    DiagnosisDao diagnosisDao;

    DiagnosisDto diagnosisDto = new DiagnosisDto(4L, 4L, 33L, "text");

    private final String URI = "/api/doctor/pet/{petId}/addDiagnosis";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("doctor33@gmail.com","user33");
    }

    //+mock, no find diagnosis
    @Test
    @DataSet(value = {"/datasets/clients.yml","/datasets/doctors.yml"}, cleanBefore = true)
    public void diagnosisShouldBeBadRequest() throws Exception {
        String diagnosis = "bla-bla-bla";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(diagnosis, headers);

        mockMvc.perform(MockMvcRequestBuilders.post(URI, entity, DiagnosisDto.class, 1001)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //+mock, add diagnosis - success
    @Test
    @DataSet(value = {"/datasets/clients.yml","/datasets/doctors.yml"}, cleanBefore = true)
    public void shouldBeCreated() throws Exception {
        int beforeCount = diagnosisDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(diagnosisDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(diagnosisDao.getAll().size());
    }
}
