package com.n26;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class N26Config extends WebMvcConfigurerAdapter {

        @Bean
        public Validator localValidatorFactoryBean() {
            return new LocalValidatorFactoryBean();
        }

}
