package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaDetalleBeneficiarioReq {
    private Long id;
    private String nombre;
    private String primer_apellido;
    private String segundo_apellido;
    private String numeroInterior;
    private String numeroExterior;
    private Integer parentesco;
    private double porcentaje;
    private String correo;
    private String nombre_completo;
    private String persona_id;
    private Integer usuario_creacion;
    private String curp;
    private String rfc;
    private String numeroCelular;
    private Boolean detalleEstatus;
    private Integer idCuenta;
    private String fecha_nac;
    private String calle;
    private String cp;
    private String colonia;
    private String ciudad;
    private String domicilio;
    private String cuenta;
}
