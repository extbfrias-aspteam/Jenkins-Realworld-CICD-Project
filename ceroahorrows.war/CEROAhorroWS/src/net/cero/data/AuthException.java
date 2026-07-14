/**
 * 
 */
package net.cero.data;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.*;

/**
 * @author rodolfo.mendez
 *bjeto para mapear un error en el webservice para enviar un codigo UNAUTHORIZED
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Actor Not Found")
public class AuthException extends Exception {
	/**
	 * variable de serialización
	 */
	private static final long serialVersionUID = 4320730264085029956L;

	
	
	
}
