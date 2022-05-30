package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.secutity.JwtToken;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.val;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
                .content(objectMapper.valueToTree(authRequestAdmin).toString()));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequestTest).toString()))
                .andReturn();
        assert (entityManager
                .createQuery("SELECT a FROM JwtToken a", JwtToken.class)
                .getResultList().size() == 1 && mvcResult.getResponse().getStatus() == 403);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void jwtTokenSaveDataBaseTest() throws Exception {
        AuthRequest authRequest = new AuthRequest("admin1@email.com", "admin");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andReturn();
        assert (mvcResult.getResponse().getContentAsString().contains(entityManager
                .createQuery("SELECT a FROM JwtToken a", JwtToken.class)
                .getResultList().get(0).getToken()));
    }
}
