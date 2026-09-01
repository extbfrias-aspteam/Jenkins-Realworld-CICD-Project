package net.cero.data;

import lombok.Data;

@Data
public class ActualizarSolicitanteTelefonoReq {
    private Integer usuarioId;
    private String solicitanteId;
    private String telefono;
    private String ip;
}
