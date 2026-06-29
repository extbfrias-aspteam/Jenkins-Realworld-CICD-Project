package mx.net.asp.procesaRendimientosCero.config;

import mx.net.asp.procesaRendimientosCero.utilerias.CustomResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    @Value("${trace.id.header}")
    private String traceIdHeader;

    @Value("${trace.id.header.signature}")
    private String traceSignatureHeader;

    @Bean
    public RestTemplate restTemplate(CustomResponseErrorHandler customResponseErrorHandler) {

        int timeout = 120000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.setErrorHandler(customResponseErrorHandler);

        // Agregar interceptor para enviar cabeceras de TraceId y Firma
        restTemplate.getInterceptors().add((request, body, execution) -> {
            String traceId = org.apache.logging.log4j.ThreadContext.get("myUuid");
            String traceSignature = org.apache.logging.log4j.ThreadContext.get("myUuidSigned");

            request.getHeaders().add(traceIdHeader, traceId != null ? traceId : "");
            request.getHeaders().add(traceSignatureHeader, traceSignature != null ? traceSignature : "");

            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
