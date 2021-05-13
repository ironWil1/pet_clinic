package com.vet24.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:/application-test.properties")
@DBUnit(schema = "public", caseInsensitiveStrategy = Orthography.LOWERCASE)
public abstract class ControllerAbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;
}
