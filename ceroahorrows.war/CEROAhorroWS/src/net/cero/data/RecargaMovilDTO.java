package net.cero.data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RecargaMovilDTO {
    @NotNull(message = "La compañia telefónica es requerido")
    private String companiaTel;

    @NotNull(message = "El número celular es requerido")
    private String numeroCelular;

    @NotNull(message = "La fecha de operación es requerido")
    @Pattern(message = "La fecha de operación no cumple con el formato YYYY-MM-DD", regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}")
    private String fechaOperacion;

    private String numeroReferencia;
    private String numeroAutorizacion;

    public String getCompaniaTel() {
        return companiaTel;
    }

    public void setCompaniaTel(String companiaTel) {
        this.companiaTel = companiaTel;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }
}
