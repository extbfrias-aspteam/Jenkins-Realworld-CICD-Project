package net.cero.ahorro.logica;

import com.google.gson.Gson;
import net.cero.data.BuscarColoniasPorCPReq;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.ColoniaDAO;
import net.cero.spring.dao.SolicitanteDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ConsultaCPLogic {

	private static Apps apps = null;
	private static final Logger log = LogManager.getLogger(ConsultaCPLogic.class);
	private static Gson gson;
	private static ColoniaDAO coloniaDAO;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			gson = new Gson();

			coloniaDAO = (ColoniaDAO) s.getApplicationContext().getBean("ColoniaDAO");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public Respuesta obtenerDatosColoniaByCp(String claveCP) {
		initialized();
		Respuesta respuesta = new Respuesta();
		List<Map<String, Object>> datos;
		log.info("Clave de request: " + claveCP);
		datos = coloniaDAO.obtenerDatosColoniaByCp(claveCP);

		if (datos == null || datos.isEmpty()) {
			respuesta.setCodigo(1);
			respuesta.setMensaje("El cp no existe");
		} else {
			respuesta.setCodigo(0);
			respuesta.setMensaje("Exito");
			respuesta.setData(gson.toJson(datos));
		}
		
		return respuesta;

	}

}
