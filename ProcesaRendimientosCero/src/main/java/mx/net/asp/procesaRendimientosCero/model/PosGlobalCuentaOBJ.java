package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosGlobalCuentaOBJ {
    private String tipoCuenta;
    private String cuentaah;
    private String clabe;
    private String personaId;
    private String estatus;
    private String cuenta_cobro;
    private String cuenta_pago;
    private String tarjetaVirtual;
    private String tarjetaFisica;
    private boolean tienePlastico;
}
