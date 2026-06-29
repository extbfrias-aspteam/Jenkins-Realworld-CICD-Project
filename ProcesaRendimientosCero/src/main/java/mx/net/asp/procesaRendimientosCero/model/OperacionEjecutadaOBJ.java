package mx.net.asp.procesaRendimientosCero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperacionEjecutadaOBJ {
	private Integer id;
	private String cuenta;
	private Integer idOperacion;
	private Integer idCatOperacionEjecutada;
	private Integer completado;
	private String observacion;
	private Timestamp fechaCreacion;
	private Timestamp fechaModificacion;
}
