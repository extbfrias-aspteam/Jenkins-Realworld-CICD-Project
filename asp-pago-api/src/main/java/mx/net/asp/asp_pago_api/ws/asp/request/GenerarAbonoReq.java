package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerarAbonoReq {
    private Long idRetiro;
    private BigDecimal monto;
    private String concepto;
    private String cuentaDestino;
    private String cuentaOrigen;
    private String producto;
    private String cveMovimiento;
    private String refNum;
    private String cveRastreo;
    private String nombreOrdenante;
}
