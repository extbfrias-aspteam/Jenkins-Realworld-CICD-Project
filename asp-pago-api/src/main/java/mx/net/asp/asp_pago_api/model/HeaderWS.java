package mx.net.asp.asp_pago_api.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderWS implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String idSesion;
	private long idEmpresa;
	private long idResponsabilidad;
	protected String usuarioClave;
	private long idUsuario;
	private long idClaseCanalAtencion;
	private long idCanalAtencion;
	private long idPuntoAtencion;
	private long idUbicacion;
	private long idSucursal;
	private long idComisionista;
	private long idTransaccion;
	protected String ipHost;
	protected String nameHost;
	private Double latitud;
	private Double longitud;
	private String idBanco;
	private String numCuenta;
}
