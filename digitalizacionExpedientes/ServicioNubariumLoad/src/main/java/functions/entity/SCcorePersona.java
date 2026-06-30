package functions.entity;

import java.sql.Date;

/**
 * Banco ASP
 * Project: eiyu
 * Class: SCcorePersona.java
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
public record SCcorePersona(String persona_id,String fecha_hora,String curp,String rfc,String nombres,String paterno,
		String materno,String fecha_nacimiento,String estado_nacimiento,String tipo_persona,Date fecha_creacion,Date fecha_modificacion,
		String usuario_creacion,String usuario_modificacion){}
