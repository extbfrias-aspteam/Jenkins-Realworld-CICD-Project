package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosReinversionOBJ {
    private String cuentaInversion;
    private String cuentaPadre;
    private String tituloReinversion;
    private Integer tipoReinversionId;
    private BigDecimal capitalReinvertir;
    private Boolean activo;
}