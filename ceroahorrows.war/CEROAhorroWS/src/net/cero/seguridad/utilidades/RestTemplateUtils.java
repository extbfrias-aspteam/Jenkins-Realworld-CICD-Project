package net.cero.seguridad.utilidades;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateUtils {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
