package mx.net.asp.asp_pago_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "jwt.endpoints")
@Component
public class JwtEndpointsExcluidosProperties {
    private List<String> excluidos;

    public List<String> getExcluidos() {
        return excluidos;
    }

    public void setExcluidos(List<String> excluidos) {
        this.excluidos = excluidos;
    }
}