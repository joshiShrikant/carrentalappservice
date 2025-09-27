package com.example.carrental.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@Primary
public class SwaggerConfig {
    @Value("${openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development server");
        Contact contact = new Contact();
        contact.setEmail("contact@carrental.com");
        contact.setName("Car Rental Support");
        contact.setUrl("https://www.carrental.com");
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
        Info info = new Info().title("Car Rental API").version("1.0").contact(contact).description("This API exposes endpoints to manage car rentals.").license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}