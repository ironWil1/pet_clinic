package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertTrue;

public class DoctorControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/doctor/pet/{petId}/addDiagnosis";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("doctor33@gmail.com", "user33");
    }

    @Test
    @DataSet(value = {"/datasets/controllers/doctorControllerTest/clients.yml",
            "/datasets/controllers/doctorControllerTest/doctors.yml"},
            cleanBefore = true)
    public void diagnosisShouldBeBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 1001)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(new DiagnosisDto(4L, 1001L, 33L, "text")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        boolean isDiagnosisNotCreated = entityManager.createQuery("SELECT CASE WHEN dd.diagnoses.size = 0 " +
                        "THEN TRUE ELSE FALSE END FROM User dd WHERE dd.email =: email", Boolean.class)
                .setParameter("email", "doctor33@gmail.com").getSingleResult();
        assertTrue("Тест не пройден! Диагноз создался, а не должен был!", isDiagnosisNotCreated);
    }

    @Test
    @DataSet(value = {"/datasets/controllers/doctorControllerTest/clients.yml",
            "/datasets/controllers/doctorControllerTest/doctors.yml"},
            cleanBefore = true)
    public void shouldBeCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(new DiagnosisDto(4L, 4L, 33L, "text")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        boolean isDiagnosisCreated = entityManager.createQuery("SELECT CASE WHEN dd.diagnoses.size = 1 " +
                        "THEN TRUE ELSE FALSE END FROM User dd WHERE dd.email =: email", Boolean.class)
                .setParameter("email", "doctor33@gmail.com").getSingleResult();
        assertTrue("Тест не пройден! Список диагнозов не равен 1", isDiagnosisCreated);
    }
}
