package com.examly.springapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI driveUOpenAPI() {

        // Use the deployed backend itself for Swagger "Try it out"
        Server proxyServer = new Server();
        proxyServer.setUrl("/");
        proxyServer.setDescription("DriveU Backend Server");

        // Team contact
        Contact teamContact = new Contact();
        teamContact.setName("DriveU Team");
        teamContact.setEmail("rishi.singh3@ltm.com");

        return new OpenAPI()
            .servers(List.of(proxyServer))
            .info(new Info()
                .title("DriveU API")
                .version("1.0.0")
                .description("REST API documentation for DriveU - "
                    + "an all-in-one platform for booking professional drivers.\n\n"
                    + "Team Members:\n"
                    + "- Rishi Singh\n"
                    + "- Nidhi Jha\n"
                           
                    + "- Bogam Revanth Kumar\n"
                  
                    + "- Om Sudhir Darkande\n"
                    + "- Yashvardhan Mishra")
                .contact(teamContact));
    }
}