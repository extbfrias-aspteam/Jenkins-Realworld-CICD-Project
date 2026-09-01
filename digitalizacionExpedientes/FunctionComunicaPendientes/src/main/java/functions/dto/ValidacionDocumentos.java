package functions.dto;

import java.util.List;

/**
 * Banco ASP Project: eiyu Class: ValidacionCurpDto.java
 *
 * Description:
 *
 * @author Herwin TR @company ICORPTTI @created Oct 23, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Oct 23, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public record ValidacionDocumentos (String clave, String t_persona, List<ValidacionesDocumentosByNivel> documenot){}