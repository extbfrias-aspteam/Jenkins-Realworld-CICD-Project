package net.cero.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	private static Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	private static Apps apps = null;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		String usuario;
		String password;

		try {			
			Apps s = Apps.getInstance();
			synchronized(Apps.class) {
				if(apps == null) // si la referencia es null ...
					apps = s;     // ... agrega la clase singleton
			}
			password= Apps.getPassAuthoriz();
			usuario= Apps.getUserAuthoriz();

			auth.inMemoryAuthentication()
			.withUser(usuario).password("{noop}"+password).roles("ADMIN");
		} 
		catch (Exception e) {
			log.error("Error en registerAuthentication, Al obtener paramentros de autentificacion: " + e.toString()) ;
		}

	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		//.antMatchers("/admin/restScoring/**").hasRole("ADMIN")
		.antMatchers("/rest/**").hasRole("ADMIN")
		.anyRequest()
		.anonymous()	        
		.and()
		.httpBasic();
		http.csrf().disable();
	}
}


/** !SecurityConfig.java */

