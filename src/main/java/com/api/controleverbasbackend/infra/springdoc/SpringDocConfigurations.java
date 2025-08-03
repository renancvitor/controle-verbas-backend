package com.api.controleverbasbackend.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Controle Verbas")
                        .version("1.0.0")
                        .description(
                                "API REST da aplicação Controle Verbas. Esta API fornece operações de criação, leitura, atualização"
                                        +
                                        " e remoção (CRUD) para os recursos de cargos, departamentos, pessoas, usuários e orçamentos"
                                        +
                                        " que compõem o funcionamento do sistema.")
                        .contact(new Contact()
                                .name("Renan C. Vitor")
                                .email("renan.vitor.cm@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://github.com/renancvitor/controle-verbas-backend/blob/main/LICENSE")));
    }
}
