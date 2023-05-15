package pl.maryana.conference;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        var openApi = new OpenAPI()
                .info(getInfo());
        return openApi;
    }

    private Info getInfo() {

        Contact contact = new Contact();
        contact.setEmail("marianamartyniuk2001@gmail.com");
        contact.setName("Maryana Martyniuk");

        return new Info()
                .title("Conference-Management REST APIs Documentation")
                .version("1.0.0")
                .contact(contact);

    }

}