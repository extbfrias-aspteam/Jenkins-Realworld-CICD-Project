package functions.entity;

import java.util.Date;

/**
 * Banco ASP
 * Project: eiyu
 * Class: ActualizaEstatusFlujo.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Dec 19, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Dec 19, 2023 Herwin: Creacion de la clase
 *
 * @category 
 *
 */

public record ActualizaEstatusFlujo(int id, Date fecha_hora, String estado_consulta, int scarchivodigital_id,
		String json_solicitud, String json_rpta, int estatus, Date fecha_creacion, Date fecha_modificacion,
		String usuario_creacion, String usuario_modificacion, String clave_mensaje_ine, String vigencia_ine, String clave_elector_ine) {
}