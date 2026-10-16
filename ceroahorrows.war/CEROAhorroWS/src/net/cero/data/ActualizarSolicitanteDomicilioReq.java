package net.cero.data;

import lombok.Data;


@Data
public class ActualizarSolicitanteDomicilioReq {


    private Integer usuarioId;

    private String solicitanteId;

    private String ip;

    private String domicilio;

    private Integer coloniaId;

    private String observaciones;

    private Integer catDomicilio1;

    private Integer catDomicilio2;

    private Integer catDomicilio3;

    private Integer catDomicilio4;

    private Integer catDomicilio5;

    private String descripcionDomicilio1;

    private String descripcionDomicilio2;

    private String descripcionDomicilio3;

    private String descripcionDomicilio4;

    private String descripcionDomicilio5;

    private ColoniasDTO colonia;
}
