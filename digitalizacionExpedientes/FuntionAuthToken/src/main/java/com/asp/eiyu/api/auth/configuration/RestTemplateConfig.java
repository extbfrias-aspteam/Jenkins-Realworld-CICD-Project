package com.asp.eiyu.api.auth.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.asp.eiyu.api.auth.FunctionAuthToken;

/**
 * 
 * Banco ASP
 * Project: eiyu
 * Class: RestTemplateConfig.java
 * Description: 
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category Config
 *
 */
public class RestTemplateConfig {
	
	/**
	 * Metodo que genera el bean de tipo RestTemplate
	 * @return retorna la instancia Restemplate
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
