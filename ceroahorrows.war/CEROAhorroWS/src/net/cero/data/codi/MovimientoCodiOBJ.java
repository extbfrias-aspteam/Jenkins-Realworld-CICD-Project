package net.cero.data.codi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoCodiOBJ {
    private String cuentaVendedor;
    private String cuentaComprador;
    private Timestamp fechaProcesamiento;
    private Double monto;
    private String estatus;
    private String folio;
    private String folioFinal;
    private String referencia;
    private String bancoOrigen;
    private String bancoDestino;
}
