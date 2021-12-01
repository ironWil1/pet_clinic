package com.vet24.web.controllers.enums;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.media.EnumsController;
import com.vet24.web.util.ReflectionUtil;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails("admin@gmail.com")
class EnumsControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private ReflectionUtil reflectionUtil;

    @Autowired
    private EnumsController enumsController;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void findAllEnums() {
        assertThat(reflectionUtil.getAllEnums()).isEqualTo(enumsController.findAllEnums().getBody());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void checkConstantList() {
        List<String> genList = reflectionUtil.getAllEnums();
        for (String name : genList) {
            assertThat((reflectionUtil.getEnumConsts(name))).isEqualTo(enumsController.getEnumNameList(name).getBody());
        }
    }

}