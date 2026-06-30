package functions.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.client.RestTemplate;

import functions.dto.ValidacionesNubariumOBJ;

/**
 * Banco ASP
 * Project: eiyu
 * Class: ActualizarCargaDocumentoInfo.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 27, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 27, 2023 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public class ActualizarCargaDocumentoInfo {

	private RestTemplate restTemplate = new RestTemplate();
	
	public Boolean nuevaValidacion(ValidacionesNubariumOBJ req) {
		Boolean insertado = false;
		try {
//			jdbcTemplatePr.update(nuevaValidacion, req.getTipoConsulta(), req.getSolicitante(), req.getDato(),
//					req.getResultado(), req.getPorcentajeCoincidencia(), req.getUsuarioCreacion(),
//					req.getCodigoValidacion());
			
//			this.restTemplate.exchange(null, null, null, null);
			insertado = true;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();

		}
		return insertado;
	}
	
}
