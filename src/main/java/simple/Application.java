package simple;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Simple API Application",
                description = "Documentation for testing the RESTful API project",
                version = "1.0.0"),
        security = @SecurityRequirement(name = "Api-key")
)
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "X-API-KEY", in = SecuritySchemeIn.HEADER)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
