package com.vet24.web.config;

import com.vet24.security.config.JwtUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

//    @Bean
//    public JwtUtils createJwtUtils() {
//        return new JwtUtils();
//    }
    @Bean
    public TokenCreator createTokenCreator(){
        return new TokenCreator();
    }

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
                                                .description("ТОКЕН КЛИЕНТА : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGllbnQxQGVtYWlsLmNvbSIsImlhdCI6MTYzMjMyNzE3MSwiZXhwIjoxNjM0MzI3MTcxfQ.j6h-ZfTXcuoLizl0yVREo263dkKu3T6D3SIKsYVQjXFKE7GlvDomG6rxeuGwPJIrZX2lZrowd3Yqy-dkV4Z25w\n"  + "\n" +
                                                        "TOКЕН ДОКТОРА : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb2N0b3IxQGVtYWlsLmNvbSIsImlhdCI6MTYyNTgzMDIwOSwiZXhwIjoxNjI3ODMwMjA5fQ.nMiVtLW98Mvosmi3KnhQiyeudKZKBeaJuJDYKCPHp_zuX8kPyTrDIL3B2Nr7zzkjT75Ebk1_AIY5aAKxw6cs7A \n " + "\n" +
                                                        "ТОКЕН МЕНЕДЖЕРА : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyMUBlbWFpbC5jb20iLCJpYXQiOjE2MzIzMjU4NDEsImV4cCI6MTYzNDMyNTg0MX0.Ys9KjhPRl2OVNEBDwsWllRLpVbYn8_XblsLz7XeLKjZ5Invt9zBpP1mt6LO2CDzCH64-gUFgejBI55YNQeIjsg \n " + "\n" +
                                                        "ТОКЕН АДМИНИСТРАТОРА : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjFAZW1haWwuY29tIiwiaWF0IjoxNjMyMzI2MTc2LCJleHAiOjE2MzQzMjYxNzZ9.zGZpb1caPiPgBm23FVIzeI-Un2RnuBcbOoElzkKn_ikr7A5vs1PQKkydigN3qLG0nwje8YpDTXZltlYxgd6sqg \n ")
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

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/user/**")
                .build();
    }
}
