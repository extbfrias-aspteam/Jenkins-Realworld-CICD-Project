package net.cero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TipoReenvioOBJ {
    private Integer id;
    private String clave;
    private String nombre;
    private Boolean activo;
}
