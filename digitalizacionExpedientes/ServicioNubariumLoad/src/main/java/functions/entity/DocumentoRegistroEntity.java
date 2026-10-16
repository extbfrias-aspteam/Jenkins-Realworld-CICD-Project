/**
 * 
 */
package functions.entity;

import java.util.Date;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroEntity.java
 *
 * Description:Mapeo de la tabla Documento registro en el cual contiene la
 * informacion recibida de los documentos
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category Entity
 *
 */
public record DocumentoRegistroEntity(int id, String codsistema, String persona_id, String codigo, String ruta_storage,
		int estado, Date fechahoracarga, String tipodocumento, String rutanotificacion, Date fecha_creacion,
		Date fecha_modificacion, String usuario_creacion, String usuario_modificacion){}