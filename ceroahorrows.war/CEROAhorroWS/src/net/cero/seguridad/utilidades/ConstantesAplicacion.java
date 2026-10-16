package net.cero.seguridad.utilidades;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@PropertySource("classpath:/configuracionAplicacion.properties")
@PropertySource("classpath:/query.properties")
public class ConstantesAplicacion {
}
