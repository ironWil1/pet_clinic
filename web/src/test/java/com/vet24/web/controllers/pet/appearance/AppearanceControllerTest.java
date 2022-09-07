package com.vet24.web.controllers.pet.appearance;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

public class AppearanceControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/appearance";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com", "admin");
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_color.yml" })
    public void testColorController() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        .param("text", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
