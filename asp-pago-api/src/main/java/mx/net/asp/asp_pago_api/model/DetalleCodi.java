package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCodi {

    private String detalle;
    private String etiqueta;
    private String urlId;
    private String conexion;
    private String respuesta;
    private String infoAdicional;
    private String estadoApp;
    private String folioCr;
    private String contenido;
}
