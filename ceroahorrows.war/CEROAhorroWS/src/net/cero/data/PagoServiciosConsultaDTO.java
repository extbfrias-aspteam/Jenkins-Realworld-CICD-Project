package net.cero.data;

import net.cero.ahorro.ws.util.WS_UTIL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PagoServiciosConsultaDTO {

    private String numeroReferencia;
    private String numeroAutorizacion;

    @NotNull(message = WS_UTIL.MENSAJE_FECHA)
    @Pattern(message = WS_UTIL.MENSAJE_FECHA_REGEX, regexp = WS_UTIL.FECHA_REGEX)
    private String fechaOperacion;

    @NotNull(message = WS_UTIL.MENSAJE_PROVEEDOR)
    private String proveedor;
    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }
}
