package mx.net.asp.procesaRendimientosCero.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.procesaRendimientosCero.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionTarjetaOrquestadorReq {
    private HeaderWS header;
    private String cuenta;
    private String concepto;
    private String importe;
    private String referenciaNumerica;
    private String medioPago;
    private String clave_rastreo;
    private String observaciones;
    private String numero_tarjeta;
    private String claveMovimiento;
}
