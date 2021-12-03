package com.vet24.web.controllers.enums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.util.ReflectionUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//Надо получить токены
@WithUserDetails("client1@email.com")
public class EnumsControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/enums";

    @Autowired
    private ReflectionUtil reflectionUtil;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void findAllEnums() throws Exception {
        MvcResult listEnumsJSON = mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<String> list = objectMapper.readValue(
                listEnumsJSON.getResponse().getContentAsString(),
                new TypeReference<>() {});
        assertThat(list).isEqualTo(reflectionUtil.getAllEnums());
    }


    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void checkConstantList() throws Exception {
        List<String> listResult = reflectionUtil.getAllEnums();

        for (String name : listResult) {
            MvcResult constListJSON = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + name)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            List<String> constListResult = objectMapper.readValue(
                    constListJSON.getResponse().getContentAsString(),
                    new TypeReference<>() {});

            assertThat(constListResult).isEqualTo((reflectionUtil.getEnumConsts(name)));
        }
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void checkIrrelevantEnum() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + "IrrelevantEnum")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}