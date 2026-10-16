package net.cero.ahorro.common;

import net.cero.spring.config.Apps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public interface DbBeans {

    class DbBeansImpl{
        private static Apps apps;
        private static final Logger log = LogManager.getLogger(DbBeansImpl.class);

        static void init(){
            Apps s = Apps.getInstance();
            synchronized (Apps.class) {
                if (apps == null) // si la referencia es null ...
                    apps = s; // ... agrega la clase singleton
            }
        }

        /**
         * Método usado para obtener beans definidos en el package src/data/resources y poder usarlo en clases con el Stereotype @Service o @Component
         *
         * @param jdbcTemplateName
         * @return
         */
        public static JdbcTemplate getJdbcInstance(String jdbcTemplateName){
            init();
            try{
                return (JdbcTemplate) apps.getApplicationContext().getBean(jdbcTemplateName);
            }catch (BeansException be){
                log.error("Error al obtener el bean {} - {}", jdbcTemplateName, be);
                throw be;
            }

        }

        public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(String namedTemplate){
            init();
            try{
                return (NamedParameterJdbcTemplate) apps.getApplicationContext().getBean(namedTemplate);
            }catch (BeansException be){
                log.error("Error al obtener el namedJdbcTemplate bean {} ", be);
                throw be;
            }
        }

        /**
         * Método usado para obtener el datasource definido en data/resources/Spring-Datasource.xml
         * @param dataSource
         * @return
         */
        public static DataSource getDataSource(String dataSource) {
            init();
            try{
                return (DataSource) apps.getApplicationContext().getBean(dataSource);
            }catch (BeansException be){
                log.error("Error al obtener el DriverManagerDataSource bean {} ", be);
                throw be;
            }
        }
    }
}
