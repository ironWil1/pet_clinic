package com.vet24.web.config;

import com.vet24.security.config.JwtUtils;
import com.vet24.service.security.JwtTokenService;
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
import org.springframework.context.annotation.Scope;

@Configuration
public class OpenApiConfig {

    JwtUtils jwtUtils;

    @Autowired
    JwtTokenService jwtTokenService;

    public OpenApiConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDescription,
                                 @Value("${application-version}") String appVersion) {

        jwtTokenService.deleteAll(jwtTokenService.getAll());
        final String securitySchemeName = "bearerAuth";

        String clientJwt = jwtUtils.generateJwtToken("client1@email.com");
        String doctorJwt = jwtUtils.generateJwtToken("doctor1@email.com");
        String managerJwt = jwtUtils.generateJwtToken("manager1@email.com");
        String adminJwt = jwtUtils.generateJwtToken("admin1@email.com");
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
                                                .description("ТОКЕН КЛИЕНТА : \n" + clientJwt + "\n\n" +
                                                        "TOКЕН ДОКТОРА : \n " + doctorJwt + "\n\n" +
                                                        "ТОКЕН МЕНЕДЖЕРА : \n " + managerJwt + "\n\n" +
                                                        "ТОКЕН АДМИНИСТРАТОРА : \n" + adminJwt)
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
