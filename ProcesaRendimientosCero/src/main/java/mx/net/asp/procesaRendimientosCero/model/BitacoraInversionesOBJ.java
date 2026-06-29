package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraInversionesOBJ {
    private String valorReferencia;
    private String claveEvento;
    private String tipoReferencia;
    private String observaciones;
    private Integer codigo;
    private Long idProcesoBitacora;
}
