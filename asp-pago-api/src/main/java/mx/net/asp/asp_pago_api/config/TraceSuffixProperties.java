package mx.net.asp.asp_pago_api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "trace.aspp")
@Component
public class TraceSuffixProperties {

    private Map<String, String> suffixes;

    public Map<String, String> getSuffixes() { return suffixes; }
    public void setSuffixes(Map<String, String> suffixes) { this.suffixes = suffixes; }
}
