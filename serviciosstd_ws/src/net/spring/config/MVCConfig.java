package net.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//@ComponentScan(basePackages = {"net.cero.ahorro.ws"})
@ComponentScan(basePackages = {"net.std.svc",
		                       "net.std.procrea.svc", 
		                       "net.std.cuentas.svc",
		                       "net.std.productos.svc",
		                       "net.std.catalogos.svc",
		                       "net.std.expediente.svc",
		                       "net.spei.svc"})
public class MVCConfig {
}

/** !MVCConfig.java */

