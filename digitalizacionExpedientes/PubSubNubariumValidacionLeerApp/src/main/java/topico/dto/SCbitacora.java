/**
 * 
 */
package topico.dto;

/**
 * Banco ASP
 * Project: eiyu
 * Class: DocumentoRegistroEntity.java
 *
 * Description:Mapeo de la tabla Documento registro en el cual contiene la informacion
 * recibida de los documentos
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
public record SCbitacora (int id, String codsistema, String persona_id, int documentos_id, String estado){}