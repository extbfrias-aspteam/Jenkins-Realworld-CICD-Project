package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidaCurpCecobanReq {
    private String curp;
    private String apellidoPaterno;
	private String nombre;
	private String sexo;
	private String fechaNacimiento;
	private String entidadFederativaNacimiento;
    private String apellidoMaterno;
}