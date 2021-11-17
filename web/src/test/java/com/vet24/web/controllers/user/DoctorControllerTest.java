package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.medicine.DiagnosisDao;
import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.user.Doctor;
import com.vet24.service.user.DoctorService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertNotNull;

@WithUserDetails(value = "doctor33@gmail.com")
public class DoctorControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    DoctorService doctorService;
    @Autowired
    DiagnosisDao diagnosisDao;

    DiagnosisDto diagnosisDto = new DiagnosisDto(4L, 4L, 33L, "text");

    private final String URI = "http://localhost:8090/api/doctor/pet/{petId}/addDiagnosis";

    //+mock, no find diagnosis
    @Test
    @DataSet(value = {"/datasets/clients.yml","/datasets/doctors.yml"}, cleanBefore = true)
    public void diagnosisShouldBeBadRequest() throws Exception {
        String diagnosis = "bla-bla-bla";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(diagnosis, headers);

        assertNotNull(mockMvc);
        mockMvc.perform(MockMvcRequestBuilders.post(URI, entity, DiagnosisDto.class, 1001)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //+mock, add diagnosis - success
    @Test
    @DataSet(value = {"/datasets/clients.yml","/datasets/doctors.yml"}, cleanBefore = true)
    public void shouldBeCreated() throws Exception {
        int beforeCount = diagnosisDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 101)
                .content(objectMapper.valueToTree(diagnosisDto).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(diagnosisDao.getAll().size());
    }
}
