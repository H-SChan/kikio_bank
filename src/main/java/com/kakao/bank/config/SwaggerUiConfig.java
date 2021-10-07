package com.kakao.bank.config;

import com.google.common.base.Predicates;
import com.kakao.bank.controller.AuthController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@EnableSwagger2
@Configuration
public class SwaggerUiConfig {

    @Bean
    public Docket api() {
        ArrayList<Parameter> global = new ArrayList<>();
        global.add(new ParameterBuilder()
                .name("Authorization")
                .description("Bearer Token")
                .parameterType("header")
                .required(false)
                .modelRef(new ModelRef("string"))
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(global)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }
}
