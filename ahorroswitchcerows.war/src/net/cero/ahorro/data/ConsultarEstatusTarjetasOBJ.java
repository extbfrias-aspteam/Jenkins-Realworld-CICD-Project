package net.cero.ahorro.data;

public class ConsultarEstatusTarjetasOBJ {
    String tipoTarjeta;
    String proveedor;
    String tarjetaPrincipal;
    private String tipoTarjetaAdicional;
    private String proveedorTarjetaAdicional;
    private String tarjetaAdicional;

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getTarjetaPrincipal() {
        return tarjetaPrincipal;
    }

    public void setTarjetaPrincipal(String tarjetaPrincipal) {
        this.tarjetaPrincipal = tarjetaPrincipal;
    }

    public String getTipoTarjetaAdicional() {
        return tipoTarjetaAdicional;
    }

    public void setTipoTarjetaAdicional(String tipoTarjetaAdicional) {
        this.tipoTarjetaAdicional = tipoTarjetaAdicional;
    }

    public String getProveedorTarjetaAdicional() {
        return proveedorTarjetaAdicional;
    }

    public void setProveedorTarjetaAdicional(String proveedorTarjetaAdicional) {
        this.proveedorTarjetaAdicional = proveedorTarjetaAdicional;
    }

    public String getTarjetaAdicional() {
        return tarjetaAdicional;
    }

    public void setTarjetaAdicional(String tarjetaAdicional) {
        this.tarjetaAdicional = tarjetaAdicional;
    }

    @Override
    public String toString() {
        return "ConsultarEstatusTarjetasOBJ{" +
                "tipoTarjeta='" + tipoTarjeta + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", tarjetaPrincipal='" + tarjetaPrincipal + '\'' +
                ", tipoTarjetaAdicional='" + tipoTarjetaAdicional + '\'' +
                ", proveedorTarjetaAdicional='" + proveedorTarjetaAdicional + '\'' +
                ", tarjetaAdicional='" + tarjetaAdicional + '\'' +
                '}';
    }
}
