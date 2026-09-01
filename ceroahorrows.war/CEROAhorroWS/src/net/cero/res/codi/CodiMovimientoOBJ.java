package net.cero.res.codi;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
public class CodiMovimientoOBJ {
    private String cuentaOrigen;
    private String cuentaDestino;
    private String fechaOperacion;
    private Double monto;
    private String estatus;
    private String bancoOrigen;
    private String bancoDestino;

    public CodiMovimientoOBJ(){}
    public CodiMovimientoOBJ(String cuentaOrigen, String cuentaDestino, String fechaOperacion, Double monto, String estatus,
                             String bancoOrigen,String bancoDestino) {
        this.cuentaOrigen = (!StringUtils.isBlank(cuentaOrigen) ?  cuentaOrigen : "");
        this.cuentaDestino = (!StringUtils.isBlank(cuentaDestino) ?  cuentaDestino : "");
        this.fechaOperacion = fechaOperacion;
        this.monto = (monto != null ? monto : 0d);
        this.estatus = (!StringUtils.isBlank(estatus) ?  estatus : "");
        this.bancoOrigen = bancoOrigen;
        this.bancoDestino = bancoDestino;
    }
}
