package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionesCuentasDatosOBJ {
	private Integer tipoTransaccionId;
	private String tipoClave;
	private Integer estatusTranssaccionId;
	private Integer formaPagoId;
	private String conciliado;
	private Integer usuarioId;
	private Integer sucursalId;
	private Integer canalId;
	private String host;
	private Integer bancoId;
}
