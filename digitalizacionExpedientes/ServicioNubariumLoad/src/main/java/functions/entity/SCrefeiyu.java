package functions.entity;

import java.sql.Date;

/**
 * Banco ASP
 * Project: eiyu
 * Class: SCrefeiyu.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Dec 20, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Dec 20, 2023 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public record SCrefeiyu(String cuenta_id,
		String nivel,
		Date fecha_hora,
		String codigo,
		String participante_id,
		Date fecha_creacion,
		Date fecha_modificacion,
		String usuario_creacion,
		String usuario_modificacion){

}
