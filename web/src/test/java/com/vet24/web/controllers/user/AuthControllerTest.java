package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.secutity.JwtToken;
import com.vet24.security.config.JwtUtils;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/auth";

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void authAdmin() throws Exception {
        AuthRequest authRequest = new AuthRequest("admin1@email.com", "admin");
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void authManager() throws Exception {
        AuthRequest authRequest = new AuthRequest("manager1@email.com", "manager");
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("MANAGER"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void authDoctor() throws Exception {
        AuthRequest authRequest = new AuthRequest("doctor1@email.com", "doctor");
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("DOCTOR"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void authClient() throws Exception {
        AuthRequest authRequest = new AuthRequest("client1@email.com", "client");
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("CLIENT"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void authBadLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest("test@email.com", "test");
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.valueToTree(authRequest).toString()))
                .andExpect(status().is(403));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void jwtTokenNotSaveDataBaseTest() throws Exception {
        AuthRequest authRequestAdmin = new AuthRequest("admin1@email.com", "admin");
        AuthRequest authRequestTest = new AuthRequest("TestNoToken@email.com", "TestNoToken");
        mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequestAdmin)));
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestTest)))
                .andExpect(status().is(403))
                .andReturn();
        assertTrue("JwtToken сохранён, тест не пройден", entityManager
                .createQuery("SELECT CASE when COUNT (*) = 1 THEN true ELSE false END FROM JwtToken ", Boolean.class).getSingleResult());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void jwtTokenSaveDataBaseTest() throws Exception {
        AuthRequest authRequest = new AuthRequest("admin1@email.com", "admin");
        AuthResponse authResponse = objectMapper.readValue(mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andReturn().getResponse().getContentAsString(), AuthResponse.class);
        assertTrue("JwtToken не сохранён, тест не пройден",
                entityManager.createQuery("SELECT case when COUNT(c) = 1 then true else false end FROM JwtToken c WHERE c.token = ?1"
                        , Boolean.class).setParameter(1, authResponse.jwtToken)
                        .getSingleResult());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void jwtTokenDeleteDataBaseTest() throws Exception {
        AuthRequest authRequest = new AuthRequest("admin1@email.com", "admin");

        AuthResponse authResponse = objectMapper.readValue(mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), AuthResponse.class);

        mockMvc.perform(post(URI + "/logout")
                        .header("Authorization", "Bearer " + authResponse.getJwtToken()))
                .andExpect(status().isOk());

        assertFalse("Токен не удален из базы данных, тест не пройден", entityManager.createQuery("SELECT case when COUNT(c) = 1 then true else false end FROM JwtToken c WHERE c.token = ?1", Boolean.class)
                .setParameter(1, authResponse.getJwtToken())
                .getSingleResult());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/jwt-token.yml"})
    public void jwtTokenIsValidTest() throws Exception {
        JwtToken jwtToken = new JwtToken(jwtUtils.generateJwtToken("admin1@email.com"));
        mockMvc.perform(post(URI + "/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtToken.getToken()))
                .andExpect(content().string("true"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/jwt-token.yml"})
    public void jwtTokenIsNoTValidTest() throws Exception {
        JwtToken badJwtToken = new JwtToken("badJwtToken.ddsNJSD");
        Boolean result = objectMapper.readValue(mockMvc.perform(post(URI + "/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJwtToken.getToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), Boolean.class);

        assertFalse("Тест не пройден", result);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void currentUserIsAdmin() throws Exception {
        String token = getAccessToken("admin1@email.com", "admin");
        mockMvc.perform(get(URI  + "/getCurrent").header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void currentUserIsClient() throws Exception {
        String token = getAccessToken("client1@email.com", "client");
        mockMvc.perform(get(URI  + "/getCurrent").header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("CLIENT"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void currentUserIsDoctor() throws Exception {
        String token = getAccessToken("doctor1@email.com", "doctor");
        mockMvc.perform(get(URI  + "/getCurrent").header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("DOCTOR"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/user/authControllerTest/users.yml"})
    public void currentUserIsManager() throws Exception {
        String token = getAccessToken("manager1@email.com", "manager");
        mockMvc.perform(get(URI  + "/getCurrent").header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("jwtToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("role").value("MANAGER"))
                .andExpect(status().isOk());
    }
}
