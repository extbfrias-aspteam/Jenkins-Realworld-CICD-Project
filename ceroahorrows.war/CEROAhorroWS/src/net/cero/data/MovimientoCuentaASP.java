package net.cero.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCuentaASP {
    private Double monto;
    private String fechaMovimiento;
    private String fechaAplicacion;
    private String descripcion;
    private String titulo;
    private String operacion;

    private DetalleMovimiento detalleMovimiento = new DetalleMovimiento();

    @Data
    @AllArgsConstructor
    @Builder
    public static class DetalleMovimiento {
        private String emisor;
        private String cuentaOrigen;
        private String beneficiario;
        private String cuentaDestino;
        private String concepto;
        private String claveRastreo;
        private String referencia;
        private String CEP;

        public DetalleMovimiento() {
            this.emisor = "";
            this.cuentaOrigen = "";
            this.beneficiario = "";
            this.cuentaDestino = "";
            this.concepto = "";
            this.claveRastreo = "";
            this.referencia = "";
            this.CEP = "";
        }
    }
}
