package net.cero.ahorro.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidaOperacionCeroReq {
    private String cuenta;
    private String claveTransaccionAhorro;
    private Double montoOperacion;
}
