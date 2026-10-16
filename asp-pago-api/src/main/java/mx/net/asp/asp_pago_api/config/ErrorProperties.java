package mx.net.asp.asp_pago_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "clave.app.error")
@Getter
@Setter
public class ErrorProperties {
    private Map<String, String> rutas;
    private List<ExclusionError> exclusiones;

    @Getter
    @Setter
    public static class ExclusionError {

        private String servicio;

        private String endpoint;

        private List<Integer> codigos;
    }
}
