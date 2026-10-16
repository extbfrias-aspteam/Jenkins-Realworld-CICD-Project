package net.cero.ahorro.data;

public class SaldoDatoOBJ {
    private String ID_Cuenta;
    private String ClaveTipoCuenta;
    private String DescripcionTipoCuenta;
    private Double Saldo;

    public String getID_Cuenta() {
        return ID_Cuenta;
    }

    public void setID_Cuenta(String ID_Cuenta) {
        this.ID_Cuenta = ID_Cuenta;
    }

    public String getClaveTipoCuenta() {
        return ClaveTipoCuenta;
    }

    public void setClaveTipoCuenta(String claveTipoCuenta) {
        ClaveTipoCuenta = claveTipoCuenta;
    }

    public String getDescripcionTipoCuenta() {
        return DescripcionTipoCuenta;
    }

    public void setDescripcionTipoCuenta(String descripcionTipoCuenta) {
        DescripcionTipoCuenta = descripcionTipoCuenta;
    }

    public Double getSaldo() {
        return Saldo;
    }

    public void setSaldo(Double saldo) {
        Saldo = saldo;
    }

    @Override
    public String toString() {
        return "SaldoDatoOBJ{" +
                "ID_Cuenta='" + ID_Cuenta + '\'' +
                ", ClaveTipoCuenta='" + ClaveTipoCuenta + '\'' +
                ", DescripcionTipoCuenta='" + DescripcionTipoCuenta + '\'' +
                ", Saldo=" + Saldo +
                '}';
    }
}
