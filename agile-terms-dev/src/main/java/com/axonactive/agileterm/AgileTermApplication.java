package com.axonactive.agileterm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "Agile Term", version = "0.0.1  "))

public class AgileTermApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileTermApplication.class, args);
    }
}
