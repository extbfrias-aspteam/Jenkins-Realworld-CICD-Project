package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlazoPorcentajeOBJ {
    private Integer rendimientoId;
    private Integer plazo;
    private Integer tasaId;
    private Double porcentaje;
}
