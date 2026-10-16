package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class edoCuentaReq {
    private String cuenta;
    private String password;
    private String dispositivoId;
    private Long fechaInicial;
    private Long fechaFinal;
}