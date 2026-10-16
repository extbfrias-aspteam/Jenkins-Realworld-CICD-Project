package net.cero.data;

import java.util.List;

public class RecargaMovilDetalle {


    private List<RecargaDetalle> recargaDetalle;

    public List<RecargaDetalle> getRecargaDetalle() {
        return recargaDetalle;
    }

    public void setRecargaDetalle(List<RecargaDetalle> recargaDetalle) {
        this.recargaDetalle = recargaDetalle;
    }

    public static class RecargaDetalle{
        private String cuentaASP;
        private String fechaOperacion;
        private String fechaAplicacion;
        private String monto;
        private String descripcionMovimiento;
        private String numeroAutorizacion;
        private String numeroReferencia;
        private String numeroCelular;
        private String companiaTel;
        private String tipoRecarga;
        private String estatus;
        private String descripcionEstatus;

        public String getFechaOperacion() {
            return fechaOperacion;
        }

        public void setFechaOperacion(String fechaOperacion) {
            this.fechaOperacion = fechaOperacion;
        }

        public String getMonto() {
            return monto;
        }

        public void setMonto(String monto) {
            this.monto = monto;
        }
        public String getNumeroCelular() {
            return numeroCelular;
        }

        public void setNumeroCelular(String numeroCelular) {
            this.numeroCelular = numeroCelular;
        }
        public String getCompaniaTel() {
            return companiaTel;
        }

        public void setCompaniaTel(String companiaTel) {
            this.companiaTel = companiaTel;
        }

        public String getEstatus() {
            return estatus;
        }

        public void setEstatus(String status) {
            this.estatus = status;
        }
        public String getCuentaASP() {
            return cuentaASP;
        }

        public void setCuentaASP(String cuentaASP) {
            this.cuentaASP = cuentaASP;
        }

        public String getFechaAplicacion() {
            return fechaAplicacion;
        }

        public void setFechaAplicacion(String fechaAplicacion) {
            this.fechaAplicacion = fechaAplicacion;
        }

        public String getDescripcionMovimiento() {
            return descripcionMovimiento;
        }

        public void setDescripcionMovimiento(String descripcionMovimiento) {
            this.descripcionMovimiento = descripcionMovimiento;
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

        public void setNumeroReferencia(String referencia) {
            this.numeroReferencia = referencia;
        }

        public String getTipoRecarga() {
            return tipoRecarga;
        }

        public void setTipoRecarga(String tipoRecarga) {
            this.tipoRecarga = tipoRecarga;
        }

        public String getDescripcionStatus() {
            return descripcionEstatus;
        }

        public void setDescripcionStatus(String descripcionStatus) {
            this.descripcionEstatus = descripcionStatus;
        }
    }
}
