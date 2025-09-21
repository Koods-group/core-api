package ci.koodysgroup.core.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("List of endpoints related to the Cinedrome project")
                        .version("1.0")
                        .description("Here you will find all the APIs that need to be implemented as part of the Cimedrone project. If you have any further questions, please contact the backend developer."));
    }
}
