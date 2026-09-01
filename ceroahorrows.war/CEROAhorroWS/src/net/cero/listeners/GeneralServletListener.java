package net.cero.listeners;


import lombok.extern.log4j.Log4j2;
import net.cero.spring.config.Apps;
import org.springframework.context.support.AbstractApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Log4j2
public class GeneralServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("******Inicio el contexto de la aplicación***************");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("******Finalizo el contexto de la aplicación***************");
        AbstractApplicationContext applicationContext =(AbstractApplicationContext)
                Apps.getInstance().getApplicationContext();
        applicationContext.destroy();
        log.info("******Se destruyo el contexto de la apliacion***************");
    }
}
