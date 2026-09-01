package net.cero.data;

import lombok.Data;

@Data
public class ActualizarSolicitanteCorreoReq {
    private Integer usuarioId;
    private String solicitanteId;
    private String correo;
    private String ip;
}
