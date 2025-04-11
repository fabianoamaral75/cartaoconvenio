package br.com.uaitagcartaoconvenio.cartaoconvenio;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(contextPath))
                .info(new Info()
                        .title("Sistema de Gestão de Cartão Convênio")
                        .version("1.0.0")
                        .description("API para gestão de cartões convênio e funcionários")
                        .contact(new Contact()
                                .name("Suporte UaiTag")
                                .email("suporte@uaitag.com.br")
                                .url("https://www.uaitag.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("cartao-convenio")
                .pathsToMatch("/**")
                .packagesToScan("br.com.uaitagcartaoconvenio.cartaoconvenio.controller")
                .addOpenApiMethodFilter(method -> 
                    !method.isAnnotationPresent(RepositoryRestResource.class))
                .build();
    }
}