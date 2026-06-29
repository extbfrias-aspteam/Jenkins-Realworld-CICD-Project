package mx.net.asp.procesaRendimientosCero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("mx.net.asp.procesaRendimientosCero")
@SpringBootApplication
@EnableScheduling
public class ProcesaRendimientosCeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcesaRendimientosCeroApplication.class, args);
	}

}
