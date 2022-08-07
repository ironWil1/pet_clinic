package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.ProfileDto;
import com.vet24.models.user.Profile;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;

public class UserProfileControllerTest extends ControllerAbstractIntegrationTest {

    private final String URL = "/api/user/profile";

    private String token;


    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com", "admin");
    }
    @Test
    @DataSet(cleanBefore = true, value = "/datasets/controllers/userProfileController/user-profile-controller.yml")
    public void getProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = "/datasets/controllers/userProfileController/user-profile-controller.yml")
    public void updateProfile() throws Exception {
        ProfileDto profileDto = new ProfileDto("test.png",
                "Vasya", "Vasilev", LocalDate.of(1970, 01, 01),
                "discord", "telegram");
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(profileDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Profile profile = entityManager.createQuery("select p from Profile p where p.firstName = :firstName", Profile.class)
                .setParameter("firstName", "Vasya").getSingleResult();
        Assert.assertEquals(profile.getFirstName(), "Vasya");
        Assert.assertEquals(profile.getLastName(), "Vasilev");
        Assert.assertEquals(profile.getBirthDate(),  LocalDate.of(1970, 01, 01));
        Assert.assertEquals(profile.getDiscordId(), "discord");
        Assert.assertEquals(profile.getTelegramId(), "telegram");
    }
}
