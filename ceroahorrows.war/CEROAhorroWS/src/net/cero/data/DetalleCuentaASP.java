package net.cero.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCuentaASP {
    private String tipoCuenta;
    private String cuenta;
    private String estatus;
}
