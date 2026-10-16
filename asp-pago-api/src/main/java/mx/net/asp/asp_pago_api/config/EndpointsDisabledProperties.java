package mx.net.asp.asp_pago_api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "banderas.endpoints")
@Component
public class EndpointsDisabledProperties {

    private Map<String, String> deshabilitados = new HashMap<>();

    public Map<String, String> getDeshabilitados() { return deshabilitados; }
    public void setDeshabilitados(Map<String, String> deshabilitados) { this.deshabilitados = deshabilitados; }
}
