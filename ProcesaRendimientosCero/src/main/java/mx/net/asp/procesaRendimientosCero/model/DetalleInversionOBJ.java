package mx.net.asp.procesaRendimientosCero.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleInversionOBJ {
    private Integer rendimientoVigenteId;
    private String titulo;
    private String cuentaInversion;
    private String cuentaPadre;
    private Integer tipoModalidadId;
    private String descModalidad;
    private Double capital;
    private Integer plazo;
    private Double tasa;
    private Double rendimiento;
    private Double total;
    private Boolean reinversion;
    @JsonIgnore
    private transient Date fechaInicioD;
    @JsonIgnore
    private transient Date fechaFinD;
    private String fechaInicio;
    private String fechaFin;
    private Integer diasFaltantes;
    private Double avance;
}