package com.vet24.web.controllers.enums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.util.ReflectionUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@WithUserDetails("client1@email.com")
public class EnumsControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "http://localhost:8080/api/enums";

    @Autowired
    private ReflectionUtil reflectionUtil;


    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void findAllEnums() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<String> list = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<String>>() {});
        assertThat(list).isEqualTo(reflectionUtil.getAllEnums());
    }


   /* @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void checkConstantList() throws ClassNotFoundException {
        List<String> genList = reflectionUtil.getAllEnums();
        for (String name : genList) {

            //assertThat(enumsController.getEnumNameList(name).getBody()).isEqualTo((reflectionUtil.getEnumConsts(name)));
        }
    }*/

}