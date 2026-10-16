package net.cero.ahorro.data;

import java.util.List;

public class RespuestaConsultarTarjetaOBJ {
    private String IDSolicitud;
    private String CodRespuesta;
    private String DescRespuesta;
    private String Tarjeta;
    private String TipoManufactura;
    private String Cuenta;
    private String CLABE;
    private String CuentaCacao;
    private String FechaVigencia;
    private String Status;
    private String DescripcionStatus;
    private String Nombre;
    private String PrimerApellido;
    private String solicitante_id;
    private List<SaldoDatoOBJ> SaldoActual;

    public String getIDSolicitud() {
        return IDSolicitud;
    }

    public void setIDSolicitud(String IDSolicitud) {
        this.IDSolicitud = IDSolicitud;
    }

    public String getCodRespuesta() {
        return CodRespuesta;
    }

    public void setCodRespuesta(String codRespuesta) {
        CodRespuesta = codRespuesta;
    }

    public String getDescRespuesta() {
        return DescRespuesta;
    }

    public void setDescRespuesta(String descRespuesta) {
        DescRespuesta = descRespuesta;
    }

    public String getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        Tarjeta = tarjeta;
    }

    public String getTipoManufactura() {
        return TipoManufactura;
    }

    public void setTipoManufactura(String tipoManufactura) {
        TipoManufactura = tipoManufactura;
    }

    public String getCuenta() {
        return Cuenta;
    }

    public void setCuenta(String cuenta) {
        Cuenta = cuenta;
    }

    public String getCLABE() {
        return CLABE;
    }

    public void setCLABE(String CLABE) {
        this.CLABE = CLABE;
    }

    public String getCuentaCacao() {
        return CuentaCacao;
    }

    public void setCuentaCacao(String cuentaCacao) {
        CuentaCacao = cuentaCacao;
    }

    public String getFechaVigencia() {
        return FechaVigencia;
    }

    public void setFechaVigencia(String fechaVigencia) {
        FechaVigencia = fechaVigencia;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescripcionStatus() {
        return DescripcionStatus;
    }

    public void setDescripcionStatus(String descripcionStatus) {
        DescripcionStatus = descripcionStatus;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPrimerApellido() {
        return PrimerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        PrimerApellido = primerApellido;
    }

    public String getSolicitante_id() {
        return solicitante_id;
    }

    public void setSolicitante_id(String solicitante_id) {
        this.solicitante_id = solicitante_id;
    }

    public List<SaldoDatoOBJ> getSaldoActual() {
        return SaldoActual;
    }

    public void setSaldoActual(List<SaldoDatoOBJ> saldoActual) {
        SaldoActual = saldoActual;
    }

    @Override
    public String toString() {
        return "RespuestaConsultarTarjetaOBJ{" +
                "IDSolicitud='" + IDSolicitud + '\'' +
                ", CodRespuesta='" + CodRespuesta + '\'' +
                ", DescRespuesta='" + DescRespuesta + '\'' +
                ", Tarjeta='" + Tarjeta + '\'' +
                ", TipoManufactura='" + TipoManufactura + '\'' +
                ", Cuenta='" + Cuenta + '\'' +
                ", CLABE='" + CLABE + '\'' +
                ", CuentaCacao='" + CuentaCacao + '\'' +
                ", FechaVigencia='" + FechaVigencia + '\'' +
                ", Status='" + Status + '\'' +
                ", DescripcionStatus='" + DescripcionStatus + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", PrimerApellido='" + PrimerApellido + '\'' +
                ", solicitante_id='" + solicitante_id + '\'' +
                ", SaldoActual=" + SaldoActual +
                '}';
    }
}
