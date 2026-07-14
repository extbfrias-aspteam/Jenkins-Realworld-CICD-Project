package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataEstadoMovimientoReq {
    private String cuenta;
    private Long fechaInicial;
    private Long fechaFinal;
}
