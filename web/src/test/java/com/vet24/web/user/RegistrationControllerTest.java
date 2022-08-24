package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;


public class RegistrationControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "/api/registration";

    @Test
    @DataSet(value = "/datasets/controllers/registrationController/user-entities.yml", cleanBefore = true)
    public void shouldBeNotAcceptableWrongEmail() throws Exception {
        RegisterDto registerDto = new RegisterDto("342354234.com", "Vera", "P",
                "Congo", "Congo");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content(objectMapper.writeValueAsString(registerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Valid email is required")));
        verify(mailService, never()).sendWelcomeMessage(anyString(), anyString(), anyString());
    }

    @Test
    @DataSet(value = "/datasets/controllers/registrationController/user-entities.yml", cleanBefore = true)
    public void shouldBeNotAcceptablePasswords() throws Exception {
        RegisterDto registerDto = new RegisterDto("342354234@gmail.com", "Vera", "P",
                "Congo", "Congo2");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content(objectMapper.writeValueAsString(registerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Passwords don't match")));
        verify(mailService, never()).sendWelcomeMessage(anyString(), anyString(), anyString());
    }

    @Test
    @DataSet(value = "/datasets/controllers/registrationController/user-entities.yml", cleanBefore = true)
    public void shouldBeCreated() throws Exception {
        RegisterDto registerDto = new RegisterDto("342354234@gmail.com", "Vera", "P",
                "Congo", "Congo");
        doNothing()
                .when(mailService)
                .sendWelcomeMessage(anyString(), anyString(), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content(objectMapper.writeValueAsString(registerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        boolean isPersistUser = entityManager.createQuery("SELECT CASE WHEN count(rdto)> 0 " +
                        "THEN TRUE ELSE FALSE END FROM User rdto WHERE rdto.email =: email", Boolean.class)
                .setParameter("email", registerDto.getEmail()).getSingleResult();
        assertTrue("Тест не пройден! Пользователь не прошел регистрацию", isPersistUser);
        verify(mailService).sendWelcomeMessage(eq("342354234@gmail.com"), eq("Vera"), anyString());
    }
}