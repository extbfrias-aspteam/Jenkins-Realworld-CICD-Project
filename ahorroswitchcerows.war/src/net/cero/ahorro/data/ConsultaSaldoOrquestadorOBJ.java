package net.cero.ahorro.data;

public class ConsultaSaldoOrquestadorOBJ {
    private HeaderWS header;
    private String numeroTarjeta;
    private String tipoTarjeta;

    public HeaderWS getHeader() {
        return header;
    }

    public void setHeader(HeaderWS header) {
        this.header = header;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    @Override
    public String toString() {
        return "ConsultaSaldoOrquestadorOBJ{" +
                "header=" + header +
                ", numeroTarjeta='" + numeroTarjeta + '\'' +
                ", tipoTarjeta='" + tipoTarjeta + '\'' +
                '}';
    }
}
