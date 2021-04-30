package com.vet24.web.config;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
                                 @Value("${application-version}") String appVersion) {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Pet Clinic API")
                        .version(appVersion)
                        .description(appDesciption)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

        openAPI.path("/api/uploads/upload-file", new PathItem()
                .post(new Operation()
                        .requestBody(new RequestBody()
                                .content(new Content()
                                        .addMediaType("multipart/form-data", new MediaType()
                                                .schema(new Schema<>()
                                                        .addRequiredItem("file")
                                                        .type("object")
                                                        .addProperties("file", new Schema<>().type("string").format("binary"))
                                                )
                                        )
                                )
                        )
                )
        );

        return openAPI;
    }
}
