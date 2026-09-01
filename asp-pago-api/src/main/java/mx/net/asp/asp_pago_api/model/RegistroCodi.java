package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroCodi {
    private Integer id;
    private String cuenta;
    private String googleId;
    private Integer codR;
    private String solicitanteId;
    private String numeroCelular;
    private String idHardware;
    private Integer consecutivo;
    private Integer appOmision;
    private String folioContrato;
    private String nombreDocumento;
    private Integer alfrescoId;
    private Integer tipoArchivoId;
    private String tokenFB;
    private String tokenFBI;
    private String contraseña;
    private Integer estatus;
    private Integer usuarioCreacion;
    private Timestamp fechaCreacion;
    private Integer usuarioModificacion;
    private Timestamp fechaModificacion;
    private Integer pinPago;
    private Integer indicadorAdquiriente;
    private String nombreAdquiriente;
    private Integer bloqueado;
    private Timestamp fechaBloqueo;
    private Integer intentos;
    private Integer bloqueosTemporales;
    private String mensajeValidacion;
    private String usuario;
    private String token;

    private String registro_manyuba;
    private String telefono_validado;
}
