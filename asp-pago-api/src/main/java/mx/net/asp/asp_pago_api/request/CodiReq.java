package mx.net.asp.asp_pago_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodiReq {
    private String numeroCuentaAhorro;
    private String dataCif;
    private Integer idCanal;
}
