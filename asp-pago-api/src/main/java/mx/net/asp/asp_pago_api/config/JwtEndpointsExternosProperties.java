package mx.net.asp.asp_pago_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "jwt.endpoints")
@Component
public class JwtEndpointsExternosProperties {
    private List<String> externos;

    public List<String> getExternos() {
        return externos;
    }

    public void setExternos(List<String> externos) {
        this.externos = externos;
    }
}