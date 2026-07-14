package net.cero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoSpeiOBJ {
    private Integer idSpei;
    private String statusOperacion;
    private String cuentaOrigen;
    private String bancoOrigen;
    private String cuentaBeneficiario;
    private String bancoDestino;
    private Timestamp fechaOperacion;
    private String claveRastreo;
    private Double monto;
}
