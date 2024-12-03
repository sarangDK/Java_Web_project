package ca.gbc.bookingservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${booking-service.version}")
    private String version;

    @Bean
    public OpenAPI bookingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("booking Service API")
                        .description("booking Service API")
                        .version(version)
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("booking Service Wiki Documentation")
                        .url("https://mycompnay.ca/booking-service/docs"));
    }
}