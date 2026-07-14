package net.cero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CuentaAspOBJ {
    private Integer id;
    private String cuenta;
    private String personaId;
    private Integer productoAhorroId;
    private Integer estatusId;
    private String clabeInterbancaria;
    private String estatusDescripcion;
}
