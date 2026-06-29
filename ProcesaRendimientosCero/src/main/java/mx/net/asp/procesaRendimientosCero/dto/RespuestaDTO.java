package mx.net.asp.procesaRendimientosCero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author FIFG
 * 
 * Clase para representar el modelado del objeto respuesta general
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class RespuestaDTO implements Serializable{
	/**
	 * Variable para serializar la clase
	 */
	@Serial
    private static final long serialVersionUID = -8173325155100829716L;
	
	// Variable para indicar el codigo de respuesta
	private int codigo;
	// Variable para indicar el mensaje de respuesta
	private String mensaje;
	// Varieble para regresar los datos de respuesta
	private String data;
}