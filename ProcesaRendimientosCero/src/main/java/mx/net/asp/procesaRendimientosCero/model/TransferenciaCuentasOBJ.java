package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaCuentasOBJ {
	private String idClienteOrigen;
	private String idClienteDestino;
	private String cuentaOrigen;
	private String nombreOrdenante;
	private String cuentaDestino;
	private String nombreBeneficiario;
	private Double monto;
	private String conceptoOrigen;
	private String conceptoDestino;
	private String tipoCuentaOrigen;
	private String tipoCuentaDestino;
	private Double latitud;
	private Double longitud;
	private String claveRastreo;
	private String referenciaNumerica;
	private TransaccionesCuentasDatosOBJ datos;
	private String tienePlasticoOrigen;
	private String tienePlasticoDestino;
}
