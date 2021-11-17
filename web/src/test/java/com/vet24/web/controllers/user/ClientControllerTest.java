package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WithUserDetails(value = "client1@email.com")
public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client";

    private final Principal principal = () -> "client1@email.com";

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void shouldGetResponseEntityClientDto_ForCurrentClient() throws Exception {
        assertNotNull(mockMvc);
        mockMvc.perform(MockMvcRequestBuilders.get(URI, ClientDto.class))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString(principal.getName())));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void uploadClientAvatarAndVerify() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
        assertNotNull(mockMvc);
        mockMvc.perform(multipart(URI + "/avatar")
                .file(mockMultipartFile).header("Content-Type", "multipart/form-data"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}