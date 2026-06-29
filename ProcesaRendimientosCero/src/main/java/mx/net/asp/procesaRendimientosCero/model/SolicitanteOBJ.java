package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitanteOBJ {
    private String numero;
    private String nombre;
    private String nombre_p;
    private String apellido_p;
    private String apellido_m;
    private String fecha_nacimiento;
    private String curp;
    private String rfc;
    private String correo;
    private String telefonoCoDi;
    private Integer colonia;
    private String domicilio;
    private String domicilioObservaciones;
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
    private String rfc1;
    private String rfc2;
    private String rfc3;
    private String sexo;
    private String tPersona;
    private String numeroCasa;
    private String celular;
    private String telefono;
    private Integer bloqueado;
}
