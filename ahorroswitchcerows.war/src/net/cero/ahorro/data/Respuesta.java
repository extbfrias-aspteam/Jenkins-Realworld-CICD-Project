package net.cero.ahorro.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {

	private int codigo;
	private String mensaje;
	private String data;
}
