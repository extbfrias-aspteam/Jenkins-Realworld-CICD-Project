package mx.net.asp.procesaRendimientosCero.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class QueryLoader {
    public static Properties loadYaml(String bd, String fileName) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("queries/" + bd + "/" + fileName));
        return yaml.getObject();
    }
}