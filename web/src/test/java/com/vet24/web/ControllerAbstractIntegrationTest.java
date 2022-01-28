package com.vet24.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.vet24.service.media.MailService;
import com.vet24.web.config.ClinicDBRider;
import com.vet24.web.controllers.user.AuthRequest;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DBUnit(schema = "public", caseInsensitiveStrategy = Orthography.LOWERCASE)
@ClinicDBRider
public abstract class ControllerAbstractIntegrationTest {

    @MockBean
    protected MailService mailService;

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Nullable
    protected String getAccessToken(String email, String password) throws Exception{
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new AuthRequest(email, password));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        return json.getString("jwtToken");
    }
}
