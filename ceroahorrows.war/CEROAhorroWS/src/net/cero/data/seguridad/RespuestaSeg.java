package net.cero.data.seguridad;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespuestaSeg {
    private Integer codigo;
    private String mensaje;
    private Object data;

    public RespuestaSeg(){
        this.codigo = 0;
        this.mensaje="OK";
        this.data = null;
    }
}
