package net.cero.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
//@ComponentScan(basePackages = {"net.cero.ahorro.ws"})
@ComponentScan(basePackages = {"net.cero.ahorro.svc", 
		                       "net.cero.pin.svc", 
		                       "net.cero.ahorroProcrea.svc", 
		                       "net.cero.ahorroCero.svc", 
		                       "net.cero.multiple.svc",
								"net.cero.ahorro.servicios",
								"net.cero.filters",
								"net.cero.utilidades"})
public class MVCConfig implements WebMvcConfigurer {
}

/** !MVCConfig.java */

