package ca.gbc.roomservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${room-service.version}")
    private String version;

    @Bean
    public OpenAPI roomServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("room Service API")
                        .description("room Service API")
                        .version(version)
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Room Service Wiki Documentation")
                        .url("https://mycompnay.ca/room-service/docs"));
    }
}