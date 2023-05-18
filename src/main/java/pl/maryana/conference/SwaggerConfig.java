package pl.maryana.conference;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

    private static final String SCHEME_NAME = "bearerScheme";
    private static final String SCHEME = "Bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        var openApi = new OpenAPI()
                .info(getInfo());
        addSecurity(openApi);
        return openApi;
    }

    private Info getInfo() {

        var contact = new Contact();
        contact.setEmail("marianamartyniuk2001@gmail.com");
        contact.setName("Maryana Martyniuk");

        return new Info()
                .title("Conference-Management REST APIs Documentation")
                .version("1.0.0")
                .contact(contact);
    }

    private void addSecurity(OpenAPI openApi) {
        Components components = createComponents();
        var securityItem = new SecurityRequirement().addList(SCHEME_NAME);

        openApi
                .components(components)
                .addSecurityItem(securityItem);
    }

    private Components createComponents() {
        var components = new Components();
        components.addSecuritySchemes(SCHEME_NAME, createSecurityScheme());

        return components;
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME);
    }

}