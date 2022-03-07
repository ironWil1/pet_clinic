package com.vet24.web.controllers.user;


import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


public class UserDoctorReviewControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "/api/user/doctor";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com","user3");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/userDoctorReviewControllerTest/doctors.yml",
            "/datasets/controllers/user/userDoctorReviewControllerTest/doctor-review.yml",
            "/datasets/controllers/user/userDoctorReviewControllerTest/clients.yml",
            "/datasets/controllers/user/userDoctorReviewControllerTest/comments.yml"})
    public void shouldBeFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{doctorId}/review", 33)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/userDoctorReviewControllerTest/doctors.yml",
            "/datasets/controllers/user/userDoctorReviewControllerTest/doctor-review.yml",
            "/datasets/controllers/user/userDoctorReviewControllerTest/clients.yml",
            "/datasets/controllers/user/userDoctorReviewControllerTest/comments.yml"})
    public void shouldBeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{doctorId}/review", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
