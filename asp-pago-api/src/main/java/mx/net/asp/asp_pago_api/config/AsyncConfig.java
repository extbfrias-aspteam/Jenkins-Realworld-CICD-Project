package mx.net.asp.asp_pago_api.config;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");

        // Decorador que copia el ThreadContext (UUID) al nuevo hilo
        executor.setTaskDecorator(runnable -> {
            Map<String, String> contextMap = ThreadContext.getImmutableContext();
            return () -> {
                try {
                    ThreadContext.putAll(contextMap);
                    runnable.run();
                } finally {
                    ThreadContext.clearAll();
                }
            };
        });

        executor.initialize();
        return executor;
    }
}