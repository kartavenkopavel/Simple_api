package simple.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springOpenApiConfig() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("api_key", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .description("Api Key access")
                                .in(SecurityScheme.In.HEADER)
                                .name("X-API-Key")
                        )          )
                .security(Collections.singletonList(
                        new SecurityRequirement().addList("api_key")))
                .info(new Info().title("Simple API Application")
                        .description("Documentation for testing the RESTful API project")
                        .version("v1.0.0"));
    }
}
