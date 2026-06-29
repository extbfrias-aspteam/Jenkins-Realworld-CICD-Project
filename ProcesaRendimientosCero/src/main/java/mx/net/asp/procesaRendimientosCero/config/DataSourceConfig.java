package mx.net.asp.procesaRendimientosCero.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Clase para configurar los DataSource y los JdbcTemplate de la base de datos
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	@Bean
	@Qualifier("ceroDataSource")
	@ConfigurationProperties(prefix = "spring.cero.datasource.hikari")
	DataSource ceroDataSource() {
		// Se crea y regresa un DataSource
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Qualifier("procreaDataSource")
	@ConfigurationProperties(prefix = "spring.procrea.datasource.hikari")
	DataSource procreaDataSource() {
		// Se crea y regresa un DataSource
		return DataSourceBuilder.create().build();
	}


	@Bean
	@Qualifier("ceroJdbcTemplate")
	JdbcTemplate ceroJdbcTemplate(@Qualifier("ceroDataSource") DataSource ceroDataSource) {
		// Se crea y se regresa un JdbcTemplate
		return new JdbcTemplate(ceroDataSource);
	}

	@Bean
	@Primary
	@Qualifier("namedCeroJdbcTemplate")
	NamedParameterJdbcTemplate namedParameterCeroJdbcTemplate(@Qualifier("ceroDataSource") DataSource ceroDataSource) {
		// Se crea y se regresa un JdbcTemplate
		return new NamedParameterJdbcTemplate(ceroDataSource);
	}

	@Bean
	@Qualifier("procreaJdbcTemplate")
	JdbcTemplate procreaJdbcTemplate(@Qualifier("procreaDataSource") DataSource procreaDataSource) {
		// Se crea y se regresa un JdbcTemplate
		return new JdbcTemplate(procreaDataSource);
	}

	@Bean
	@Qualifier("namedProcreaJdbcTemplate")
	NamedParameterJdbcTemplate procreaNamedJdbcTemplate(@Qualifier("procreaDataSource") DataSource procreaDataSource) {
		// Se crea y se regresa un JdbcTemplate
		return new NamedParameterJdbcTemplate(procreaDataSource);
	}

	// Configuración de TransactionManager para ceroDataSource
	@Bean
	public PlatformTransactionManager transactionManagerCero(@Qualifier("ceroDataSource") DataSource ceroDataSource) {
		return new DataSourceTransactionManager(ceroDataSource);
	}

	// Configuración de TransactionManager para procreaDataSource
	@Bean
	public PlatformTransactionManager transactionManagerProcrea(@Qualifier("procreaDataSource") DataSource procreaDataSource) {
		return new DataSourceTransactionManager(procreaDataSource);
	}
}