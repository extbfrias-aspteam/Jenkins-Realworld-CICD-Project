package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReinversionPendOBJ {
    private Long Id;
    private String cuentaInversion;
    private String cuentaPadre;
    private String estatus;
    private LocalDate fechaPlazo;
    private LocalDate fechaCalc;
}
