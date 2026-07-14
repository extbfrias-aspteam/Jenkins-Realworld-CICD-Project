package mx.net.asp.asp_pago_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("mx.net.asp.asp_pago_api")
@SpringBootApplication
public class AspPagoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AspPagoApiApplication.class, args);
	}

}