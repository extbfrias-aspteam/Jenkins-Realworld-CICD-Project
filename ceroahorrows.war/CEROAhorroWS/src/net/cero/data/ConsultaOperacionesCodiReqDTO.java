package net.cero.data;

import net.cero.ahorro.ws.util.WS_UTIL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ConsultaOperacionesCodiReqDTO {

    @NotNull(message = WS_UTIL.MENSAJE_FECHA_INICIO)
    @Pattern(message = WS_UTIL.FECHA_INICIO_REGEX, regexp = WS_UTIL.FECHA_REGEX)
    private String fechaInicio;

    @NotNull(message = WS_UTIL.MENSAJE_FECHA_FIN)
    @Pattern(message = WS_UTIL.FECHA_INICIO_REGEX, regexp = WS_UTIL.FECHA_REGEX)
    private String fechaFin;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
