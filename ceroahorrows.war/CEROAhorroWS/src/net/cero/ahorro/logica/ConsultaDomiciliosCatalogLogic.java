package net.cero.ahorro.logica;

import com.google.gson.Gson;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.DomiciliosCatalogDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ConsultaDomiciliosCatalogLogic {

	private static Apps apps = null;
	private static final Logger log = LogManager.getLogger(ConsultaDomiciliosCatalogLogic.class);
	private static Gson gson;
	private static DomiciliosCatalogDAO domiciliosCatalogDAO;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			gson = new Gson();

			domiciliosCatalogDAO = (DomiciliosCatalogDAO) s.getApplicationContext().getBean("DomiciliosCatalogDAO");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public Respuesta consultaDomiciliosCatalog() {
		log.info("Entra a consultaDomiciliosCataloglogic");
		initialized();
		Respuesta respuesta = new Respuesta();
		List<Map<String, Object>> datos;
		
		datos = domiciliosCatalogDAO.consultaDomiciliosCatalog();
		log.info("Respuesta de bdd catalog: " + datos.toString());
		if (datos == null || datos.isEmpty()) {
			respuesta.setCodigo(1);
			respuesta.setMensaje("No se encontró información del catalogo de domicilios");
		} else {
			respuesta.setCodigo(0);
			respuesta.setMensaje("Exito");
			respuesta.setData(gson.toJson(datos));
		}
		
		return respuesta;

	}

}
