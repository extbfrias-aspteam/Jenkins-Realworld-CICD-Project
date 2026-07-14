package net.cero.data;

public class PagoServicioDTO {
    private String cuentaASP;
    private String descripcionMovimiento;
    private String emisor = "";
    private String movimiento;
    private String numeoReferencia;
    private String fechaOperacion;
    private String fechaAplicacion;
    private Double monto;
    private String proveedor;
    private String estatus;
    private String mensajeEstatus;
    private String numeroAutorizacion;

    public String getReferencia() {
        return numeoReferencia;
    }

    public void setReferencia(String referencia) {
        this.numeoReferencia = referencia;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getCuentaASP() {
        return cuentaASP;
    }

    public void setCuentaASP(String cuentaASP) {
        this.cuentaASP = cuentaASP;
    }

    public String getDescripcionMovimiento() {
        return descripcionMovimiento;
    }

    public void setDescripcionMovimiento(String descripcionMovimiento) {
        this.descripcionMovimiento = descripcionMovimiento;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getMensajeEstatus() {
        return mensajeEstatus;
    }

    public void setMensajeEstatus(String mensajeEstatus) {
        this.mensajeEstatus = mensajeEstatus;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }
}
