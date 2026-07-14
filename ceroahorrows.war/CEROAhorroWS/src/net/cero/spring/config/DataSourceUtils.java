package net.cero.spring.config;


import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Log4j2
@Configuration
public class DataSourceUtils {
    @Autowired
    private Environment env;
    @Bean
    @Qualifier("ceroDataSource")
    public DataSource ceroDataSource() {
        // Se crea y regresa un DataSource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.cero.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.cero.jdbc-url"));
        dataSource.setUsername(env.getProperty("spring.cero.username"));
        dataSource.setPassword(env.getProperty("spring.cero.password"));

        dataSource.setMaximumPoolSize(env.getProperty("spring.cero.pooling.maximum-pool-size",Integer.class));
        dataSource.setMinimumIdle(env.getProperty("spring.cero.pooling.minimumIdle",Integer.class));
        dataSource.setIdleTimeout(env.getProperty("spring.cero.pooling.idleTimeout",Long.class));
        dataSource.setMaxLifetime(env.getProperty("spring.cero.pooling.max-lifetime",Integer.class));
        dataSource.setConnectionTimeout(env.getProperty("spring.cero.pooling.connection-timeout",Integer.class));
        dataSource.setPoolName(env.getProperty("spring.cero.pooling.pool-name"));
        return dataSource;
    }

    /**
     * Metodo para obtener un DataSource a partir de las properties spring.cero
     * @return objeto DataSource con la conexion creada ceroDataSource
     */
    @Bean
    @Qualifier("ceroJdbcTemplate")
    public JdbcTemplate ceroJdbcTemplate(@Qualifier("ceroDataSource") DataSource ceroDataSource) {
        // Se crea y se regresa un JdbcTemplate
        return new JdbcTemplate(ceroDataSource);
    }

    @Primary
    @Bean
    @Qualifier("namedCeroJdbcTemplate")
    public NamedParameterJdbcTemplate namedCeroJdbcTemplate(@Qualifier("ceroDataSource") DataSource ceroDataSource) {
        // Se crea y se regresa un JdbcTemplate
        return new NamedParameterJdbcTemplate(ceroDataSource);
    }

    /**
     * Metodo para obtener un DataSource a partir de las properties spring.procrea
     * @return objeto DataSource con la conexion creada procreaDataSource
     */
    @Bean
    @Qualifier("procreaDataSource")
    public DataSource procreaDataSource() {
        // Se crea y regresa un DataSource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.procrea.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.procrea.jdbc-url"));
        dataSource.setUsername(env.getProperty("spring.procrea.username"));
        dataSource.setPassword(env.getProperty("spring.procrea.password"));

        dataSource.setMaximumPoolSize(env.getProperty("spring.procrea.pooling.maximum-pool-size",Integer.class));
        dataSource.setMinimumIdle(env.getProperty("spring.procrea.pooling.minimumIdle",Integer.class));
        dataSource.setIdleTimeout(env.getProperty("spring.procrea.pooling.idleTimeout",Long.class));
        dataSource.setMaxLifetime(env.getProperty("spring.procrea.pooling.max-lifetime",Integer.class));
        dataSource.setConnectionTimeout(env.getProperty("spring.procrea.pooling.connection-timeout",Integer.class));
        dataSource.setPoolName(env.getProperty("spring.procrea.pooling.pool-name"));

        return dataSource;
    }

    /**
     * Metodo para obtener un JdbcTemplate a partir del DataSource
     * @param procreaDataSource objeto DataSource del tipo procreaDataSource
     * @return objeto JdbcTemplate para realizar operaciones en la bd
     */
    @Bean
    @Qualifier("procreaJdbcTemplate")
    public JdbcTemplate procreaJdbcTemplate(@Qualifier("procreaDataSource") DataSource procreaDataSource) {
        // Se crea y se regresa un JdbcTemplate
        return new JdbcTemplate(procreaDataSource);
    }

    /**
     * Metodo para obtener un DataSource a partir de las properties spring.procrea
     * @return objeto DataSource con la conexion creada procreaDataSource
     */
    @Bean
    @Qualifier("izelStiDataSource")
    public DataSource izelStiDataSource() {
        // Se crea y regresa un DataSource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.izelsti.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.izelsti.jdbc-url"));
        dataSource.setUsername(env.getProperty("spring.izelsti.username"));
        dataSource.setPassword(env.getProperty("spring.izelsti.password"));

        dataSource.setMaximumPoolSize(env.getProperty("spring.izelsti.pooling.maximum-pool-size",Integer.class));
        dataSource.setMinimumIdle(env.getProperty("spring.izelsti.pooling.minimumIdle",Integer.class));
        dataSource.setIdleTimeout(env.getProperty("spring.izelsti.pooling.idleTimeout",Long.class));
        dataSource.setMaxLifetime(env.getProperty("spring.izelsti.pooling.max-lifetime",Integer.class));
        dataSource.setConnectionTimeout(env.getProperty("spring.izelsti.pooling.connection-timeout",Integer.class));
        dataSource.setPoolName(env.getProperty("spring.izelsti.pooling.pool-name"));

        return dataSource;
    }

    /**
     * Metodo para obtener un JdbcTemplate a partir del DataSource
     * @param izelStiDataSource objeto DataSource del tipo procreaDataSource
     * @return objeto JdbcTemplate para realizar operaciones en la bd
     */
    @Bean
    @Qualifier("izelStiDataSourceTemplate")
    public JdbcTemplate izelSTIJdbcTemplate(@Qualifier("izelStiDataSource") DataSource izelStiDataSource) {
        // Se crea y se regresa un JdbcTemplate
        return new JdbcTemplate(izelStiDataSource);
    }

    @Bean
    @Qualifier("namedIzelStiDataSourceTemplate")
    public NamedParameterJdbcTemplate namedIzelSTIJdbcTemplate(@Qualifier("izelStiDataSource") DataSource izelStiDataSource) {
        // Se crea y se regresa un JdbcTemplate
        return new NamedParameterJdbcTemplate(izelStiDataSource);
    }
}
