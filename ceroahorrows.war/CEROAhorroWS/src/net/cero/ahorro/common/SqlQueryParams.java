package net.cero.ahorro.common;

public class SqlQueryParams {
    protected int type;
    protected String nombre;
    protected Object value;

    public SqlQueryParams(int type, Object value){
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
