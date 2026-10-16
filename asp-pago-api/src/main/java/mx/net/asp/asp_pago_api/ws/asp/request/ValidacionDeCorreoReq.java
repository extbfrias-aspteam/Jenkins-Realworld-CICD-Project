package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionDeCorreoReq {
    private Long id;
    private String correoElectronico;
    private String token;
    private String rfc;
    private String curp;
    private String accion;
    private String fh;
    private String estado;
    private String nombre;
}
