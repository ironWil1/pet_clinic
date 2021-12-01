package com.vet24.web.controllers.enums;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.media.EnumsController;
import com.vet24.web.util.ReflectionUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WithUserDetails("client1@email.com")
public class EnumsControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "http://localhost:8080/api/enums";

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
    public void checkConstantList() throws ClassNotFoundException {
        List<String> genList = reflectionUtil.getAllEnums();
        for (String name : genList) {
            assertThat((reflectionUtil.getEnumConsts(name))).isEqualTo(enumsController.getEnumNameList(name).getBody());
        }
    }

}