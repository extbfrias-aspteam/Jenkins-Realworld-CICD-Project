package mx.net.asp.asp_pago_api.config;

import mx.net.asp.asp_pago_api.service.TimeoutConfigService;
import mx.net.asp.asp_pago_api.utilerias.CustomResponseErrorHandler;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    private final TimeoutConfigService timeoutConfigService;

    @Value("${trace.id.header}")
    private String traceIdHeader;

    @Value("${trace.id.header.signature}")
    private String traceSignatureHeader;

    @Autowired
    public AppConfig(TimeoutConfigService timeoutConfigService) {
        this.timeoutConfigService = timeoutConfigService;
    }

    @Bean
    public RestTemplate restTemplate(CustomResponseErrorHandler customResponseErrorHandler) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = createClientHttpRequestFactory();
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

    private HttpComponentsClientHttpRequestFactory createClientHttpRequestFactory() {
        int connectTimeout = timeoutConfigService.getConnectTimeout();
        int readTimeout = timeoutConfigService.getReadTimeout();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(HttpClients.createDefault());
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
}
