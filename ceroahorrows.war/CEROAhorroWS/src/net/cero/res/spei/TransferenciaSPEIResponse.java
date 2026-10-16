package net.cero.res.spei;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaSPEIResponse {
    private String cuentaOrigen;
    private String cuentaBeneficiario;
    private Double monto;
    private String bancoOrigen;
    private String bancoDestino;
    private String fechaOperacion;
    private String claveRastreo;
}
