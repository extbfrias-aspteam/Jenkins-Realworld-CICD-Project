package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendimientoPendOBJ {
    private Long Id;
    private String cuentaInversion;
    private String cuentaPadre;
    private BigDecimal monto;
    private Integer idMov;
    private String estatus;
    private LocalDate fechaRend;
}
