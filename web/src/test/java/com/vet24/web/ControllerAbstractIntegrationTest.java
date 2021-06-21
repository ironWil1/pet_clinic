package com.vet24.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(schema = "public", caseInsensitiveStrategy = Orthography.LOWERCASE)
public abstract class ControllerAbstractIntegrationTest {

    @Autowired
    protected ApplicationRestTemplate testRestTemplate;

    @Autowired
    protected MockMvc mockMvc;
}
