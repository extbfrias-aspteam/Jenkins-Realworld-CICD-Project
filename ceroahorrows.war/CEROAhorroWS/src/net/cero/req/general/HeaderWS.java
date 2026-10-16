package net.cero.req.general;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Clase que representa el modelado del objeto con los datos de entrada para las peticiones de los servicios
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderWS implements Serializable {

	/**
	 * Variable para serializar la clase
	 */
	private static final long serialVersionUID = -8333732507463499246L;
	
	private String idSesion;
	private long idEmpresa;
	private long idResponsabilidad;
	@NotNull(message = "El campo %s es obligatorio")
	@NotBlank(message = "El campo %s no puede estar vacio")
	protected String usuarioClave;
	@NotNull(message = "El campo %s es obligatorio")
	private long idUsuario; // idUsuario = 100
	private long idClaseCanalAtencion;
	private long idCanalAtencion; // idCanalAtencion = 2
	private long idPuntoAtencion;
	private long idUbicacion;
	private long idSucursal;
	private long idComisionista;
	private long idTransaccion;
	@NotNull(message = "El campo %s es obligatorio")
	@NotBlank(message = "El campo %s no puede estar vacio")
	protected String ipHost;
	protected String nameHost;
}