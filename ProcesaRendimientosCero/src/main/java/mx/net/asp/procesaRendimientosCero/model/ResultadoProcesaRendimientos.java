package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoProcesaRendimientos {
    private String cuentaInversion;
    private String cuentaPadre;
    private String proceso;
    private String resultado;
    private String mensaje;
}
