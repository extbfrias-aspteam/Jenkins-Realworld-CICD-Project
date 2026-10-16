package net.cero.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {"net.cero.ahorro.*","net.cero.ahorro.spei.enviospei"
        ,"net.cero.spring.dao","net.cero.spring.config","net.cero.seguridad.utilidades","net.cero.ahorro.logica"
        ,"net.cero.quartz","net.cero.filters","net.cero.handler.exceptions"})
public class MVCConfig 
{
}

/** !MVCConfig.java */

