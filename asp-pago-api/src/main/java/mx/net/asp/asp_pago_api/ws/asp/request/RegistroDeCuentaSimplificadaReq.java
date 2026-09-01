package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.GeolocalizacionOBJ;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDeCuentaSimplificadaReq {
    private HeaderWS header;
    private String primerNombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rfc;
    private String curp;
    private String celular;
    private String email;
    private String ine;
    private String codigoPromocion;
    private String validacionOcrReq;
    private String domicilio;
    private String codigoPostal;
    private Integer coloniaId;
    private String coloniaNombre;
    private Boolean ineValidado;
    private String localidad;
    private String sexo;
    private String codigoAutorizacion;
    private String aceptaTerminos;
    private String aceptaUsoBiometricos;
    private String password;
    private String actuoCuentaPropia;
    private GeolocalizacionOBJ geolocalizacion;
    private Boolean validarInfo;
}
