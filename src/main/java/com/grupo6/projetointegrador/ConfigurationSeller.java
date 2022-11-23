package com.grupo6.projetointegrador;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigurationSeller {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
