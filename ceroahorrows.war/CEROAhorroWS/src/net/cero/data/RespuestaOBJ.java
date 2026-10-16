package net.cero.data;

public class RespuestaOBJ {
    private int codigo;
    private String mensaje;
    private Object data;
    public RespuestaOBJ(){

    }
    public RespuestaOBJ(int codigo, String mensaje){
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
