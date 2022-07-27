package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;

public class ClientNewControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "/api/client/news";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/clientNewController/user_entities.yml",
            "/datasets/controllers/user/clientNewController/news.yml",
            "/datasets/controllers/user/clientNewController/news_pictures.yml"
    })
    public void getAllNews_ShouldShowAllNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("news"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("PROMOTION"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)));
    }
}
