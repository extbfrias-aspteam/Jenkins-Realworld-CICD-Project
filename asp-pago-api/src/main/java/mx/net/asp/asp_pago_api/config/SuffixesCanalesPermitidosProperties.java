package mx.net.asp.asp_pago_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "suffixes.canales")
@Component
public class SuffixesCanalesPermitidosProperties {
    private List<String> permitidos;

    public List<String> getPermitidos() {
        return permitidos;
    }

    public void setPermitidos(List<String> permitidos) {
        this.permitidos = permitidos;
    }
}