package net.cero.ahorro.logica;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.ws.util.BloqueoDesbloqueo;
import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.BloqueoDesbloqueoAppDao;
import net.cero.spring.dao.excepcion.DaoException;

@Log4j2
public class BloqueoDesbloqueoAppLogic {

    private static Apps apps = null;
    private static BloqueoDesbloqueoAppDao bloqueoDesbloqueoAppDao;

    private static void initialized() {

	try {
	    Apps s = Apps.getInstance();
	    synchronized (Apps.class) {
		if (apps == null) // si la referencia es null ...
		    apps = s; // ... agrega la clase singleton
	    }
	    bloqueoDesbloqueoAppDao = (BloqueoDesbloqueoAppDao) s.getApplicationContext()
		    .getBean("BloqueoDesbloqueoAppDao");
	} catch (Exception e) {
	    log.error("Error al obtener el bean ", e);
	}
    }

    public Respuesta bloqueoApp(Map<String, String> body) {

	return desbloqueoApp(BloqueoDesbloqueo.BLOQUEA, body);
    }

    /**
     * RQ000069 se considera tomar la lista para desbloquear
     * 
     * @param bloqueaDesbloquea
     * @param body
     * @return
     */
    public Respuesta desbloqueoApp(BloqueoDesbloqueo bloqueaDesbloquea, Map<String, String> body) {
	initialized();
	Respuesta respuesta = new Respuesta();
	int operacion = 0;
	boolean operacionb=false;
	String telefono = body.get("telefono");
	String cuenta = body.get("cuenta");

	respuesta.setData("");
	respuesta.setCodigo(4);
	if (null==telefono) {
	    respuesta.setMensaje("No se ha proporcionado un número de teléfono");
	} else if (null==cuenta) {
	    respuesta.setMensaje("No se ha proporcionado un número de cuenta");
	} else {
	    if (bloqueaDesbloquea == BloqueoDesbloqueo.BLOQUEA) {
		operacion = WS_UTIL.BLOQUEA_ASP_PAGO_APP;
		operacionb=true;
		respuesta.setMensaje("Bloqueo exitoso");
	    } else {
		operacion = WS_UTIL.DESBLOQUEA_ASP_PAGO_APP;
		operacionb=false;
		respuesta.setMensaje("Desbloqueo exitoso");
	    }

	    try {
		List<Map<String, Object>> solicitanteMap = bloqueoDesbloqueoAppDao.consultaSolicitantel(telefono);
		String idSolicitante = obtieneSolicitante(solicitanteMap, cuenta);
		validaBloqueoDesbloqueo(idSolicitante, cuenta, operacionb);
		bloqueoDesbloqueoAppDao.desbloqueoTotalApp(idSolicitante, cuenta, operacion);
		respuesta.setCodigo(0);

	    } catch (DaoException e) {
		respuesta.setMensaje(e.getMessage());
	    }
	}
	return respuesta;
    }

    /**
     * 
     * @param solicitanteMap
     * @return
     * @throws DaoException
     */
    private String obtieneSolicitante(List<Map<String, Object>> solicitanteMap, String cuenta) throws DaoException {
	List<Map<String, Object>> solicitantes = bloqueoDesbloqueoAppDao
		.consultaRegistroCodiPorCuentaYSolicitantes(cuenta, solicitanteMap);
	if (solicitantes.isEmpty()) {
	    throw new DaoException("No es posible desbloquer la App, teléfono-cuenta no existe");
	} else if (solicitantes.size() != 1) {
	    throw new DaoException("No es posible desbloquer la App, la cuenta esta registrada a varios solicitantes");
	}
	return solicitantes.get(0).get("solicitante_id").toString();
    }

    /**
     * Valida si esta bloqueado/desbloqueado, si no coincide con el valor esperado
     * lanza una excepcion
     * 
     * @param idSolicitante
     * @param cuenta
     * @param bloqueo
     * @throws DaoException
     */
    private void validaBloqueoDesbloqueo(String idSolicitante, String cuenta, boolean bloqueo) throws DaoException {
	String bloqueado = bloqueoDesbloqueoAppDao.consultaBloqueoDesbloqueoApp(idSolicitante, cuenta);
	if (null == bloqueado) {
	    throw new DaoException("No se puede determinar el estatus");
	}
	Integer iBloqueado = Integer.valueOf(bloqueado);
	if (bloqueo) {
	    if (iBloqueado >= WS_UTIL.BLOQUEA_ASP_PAGO_APP)
		throw new DaoException("La cuenta en ASP Pago ya ha sido bloqueada");
	} else if (!bloqueo && (iBloqueado == WS_UTIL.DESBLOQUEA_ASP_PAGO_APP)) {
	    throw new DaoException("La cuenta ASP Pago se encuentra activa");
	}
    }

}
