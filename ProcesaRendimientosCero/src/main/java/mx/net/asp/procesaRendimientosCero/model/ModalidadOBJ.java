package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModalidadOBJ {
    private Integer tipoModalidadId;
    private String descripcion;
    private Double montoMin;
    private Double montoMax;
}