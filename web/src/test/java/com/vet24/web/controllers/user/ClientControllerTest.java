package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client";
    private ClassPathResource classPathResource;
    private MockMultipartFile mockMultipartFile;
    private String token;


    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    @Before
    public void setData() throws IOException {
        classPathResource = new ClassPathResource("test.png");
        mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/clientControllerTest/user-entities.yml",
            "/datasets/controllers/clientControllerTest/pet-entities.yml"})
    public void shouldGetResponseEntityClientDtoForCurrentClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString("client1@email.com")));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/clientControllerTest/user-entities.yml",
            "/datasets/controllers/clientControllerTest/pet-entities.yml"})
    public void uploadClientAvatarAndVerify() throws Exception {
        mockMvc.perform(multipart(URI + "/avatar")
                        .file(mockMultipartFile)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk());
        boolean isAvatarUpload = entityManager.createQuery("SELECT CASE WHEN p.avatarUrl is not null " +
                        "THEN TRUE ELSE FALSE END FROM Profile p WHERE p.user.email =: email", Boolean.class)
                .setParameter("email", "client1@email.com").getSingleResult();
        assertTrue("Тест не пройдет! Аватар не загрузился", isAvatarUpload);
    }
}