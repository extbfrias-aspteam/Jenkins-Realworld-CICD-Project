package mx.net.asp.asp_pago_api.ws.asp.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3867601014655143723L;


	private Integer anio;

	private Integer numMes;

	private String cuentaInversion;

}
