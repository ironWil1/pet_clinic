package com.vet24.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDescription,
                                 @Value("${application-version}") String appVersion) {

        final String securitySchemeName = "bearerAuth";
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Pet Clinic API")
                        .version(appVersion)
                        .description(appDescription)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

        return openAPI
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .description("Токен клиента : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGllbnQxQGVtYWlsLmNvbSIsImlhdCI6MTYyNTY4MDc5OCwiZXhwIjoxNjI1Njk4Nzk4fQ.pRlL6wYk5bmhTJYMOMo_GVoJWXB7-EqU4YGUao8YwnFpm7TplSozoOTtMZV6KEksSrmO2jAylkzhKoPAkKnbbQ                                 Токен доктора : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb2N0b3IxQGVtYWlsLmNvbSIsImlhdCI6MTYyNTY4MDgzNiwiZXhwIjoxNjI1Njk4ODM2fQ.3pn85NuzlbpN1unbf4q8VkZ-yhpLKx-b3w7A4MzpLGNKyMA2l3LU0Yzlksd7n_NHgZESTAciHbocd6US0gQIJQ" )
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }

    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("client")
                .pathsToMatch("/api/client/**")
                .build();

    }

    @Bean
    public GroupedOpenApi doctorApi() {
        return GroupedOpenApi.builder()
                .group("doctor")
                .pathsToMatch("/api/doctor/**")
                .build();
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
                .group("manager")
                .pathsToMatch("/api/manager/**")
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all controllers")
                .pathsToMatch("/**")
                .build();
    }
}
