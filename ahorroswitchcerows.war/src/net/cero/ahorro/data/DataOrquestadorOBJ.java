package net.cero.ahorro.data;

import java.math.BigDecimal;

public class DataOrquestadorOBJ {
    private Long IDSolicitud;
    private String Tarjeta;
    private Double Importe;
    private Double SaldoFinal;
    private String CodRespuesta;
    private String DescRespuesta;
    private Integer movimientoId;

    public Long getIDSolicitud() {
        return IDSolicitud;
    }

    public void setIDSolicitud(Long IDSolicitud) {
        this.IDSolicitud = IDSolicitud;
    }

    public String getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        Tarjeta = tarjeta;
    }

    public Double getImporte() {
        return Importe;
    }

    public void setImporte(Double importe) {
        Importe = importe;
    }

    public Double getSaldoFinal() {
        return SaldoFinal;
    }

    public void setSaldoFinal(Double saldoFinal) {
        SaldoFinal = saldoFinal;
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

    public Integer getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(Integer movimientoId) {
        this.movimientoId = movimientoId;
    }

    @Override
    public String toString() {
        return "DataOrquestadorOBJ{" +
                "IDSolicitud=" + IDSolicitud +
                ", Tarjeta='" + Tarjeta + '\'' +
                ", Importe=" + Importe +
                ", SaldoFinal=" + SaldoFinal +
                ", CodRespuesta='" + CodRespuesta + '\'' +
                ", DescRespuesta='" + DescRespuesta + '\'' +
                ", movimientoId=" + movimientoId +
                '}';
    }
}
