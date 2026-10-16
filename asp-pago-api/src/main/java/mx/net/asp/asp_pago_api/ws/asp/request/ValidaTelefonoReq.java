package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidaTelefonoReq {
    private String telefono;
    private String token;
    private Integer validaCuenta;
    private String curp;
    private String idDispositivo;
}
