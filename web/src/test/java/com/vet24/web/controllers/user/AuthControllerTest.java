package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.secutity.JwtToken;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerAbstractIntegrationTest {
    @Autowired
    AuthController authController;

    final String URI = "/api/auth";

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void authAdmin() throws Exception {
        AuthRequest authRequest = new AuthRequest("admin1@email.com", "admin");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void authManager() throws Exception {
        AuthRequest authRequest = new AuthRequest("manager1@email.com", "manager");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("MANAGER"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void authDoctor() throws Exception {
        AuthRequest authRequest = new AuthRequest("doctor103@email.com", "doctor");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("DOCTOR"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void authClient() throws Exception {
        AuthRequest authRequest = new AuthRequest("client1@email.com", "client");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("CLIENT"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void authBadLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest("test@email.com", "test");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(status().is(403));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void jwtTokenNotSaveDataBaseTest() throws Exception {
        AuthRequest authRequestAdmin = new AuthRequest("admin1@email.com", "admin");
        AuthRequest authRequestTest = new AuthRequest("TestNoToken@email.com", "TestNoToken");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequestAdmin)));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestTest)))
                .andReturn();

        assertTrue(String.valueOf(entityManager
                .createQuery("SELECT CASE when COUNT(*) = 1 THEN true ELSE false END FROM JwtToken ")
                .getSingleResult()), true);
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(403);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void jwtTokenSaveDataBaseTest() throws Exception {
        AuthRequest authRequest = new AuthRequest("admin1@email.com", "admin");
        AuthResponse authResponse = objectMapper.readValue(mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andReturn().getResponse().getContentAsString(), AuthResponse.class);
        assertTrue(entityManager.find(JwtToken.class, authResponse.jwtToken).getToken(), true);
    }
}
