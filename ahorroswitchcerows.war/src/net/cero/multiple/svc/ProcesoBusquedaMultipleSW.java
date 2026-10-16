package net.cero.multiple.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cero.ahorroCero.servicios.ServiciosCuentasMultiplesCeroWS;
import net.cero.ahorroProcrea.servicios.ServiciosSolicitanteBasicoPrWS;
import net.cero.multiple.servicios.ServiciosBusquedaAhoCeroWS;
import net.cero.multiple.servicios.ServiciosBusquedaAhoProcreaWS;
import net.cero.multiple.servicios.ServiciosBusquedaComisionistaWS;
import net.cero.multiple.servicios.ServiciosBusquedaCreCeroWS;
import net.cero.multiple.servicios.ServiciosBusquedaCreProcreaWS;
import net.cero.multiple.servicios.ServiciosBusquedaSolicitanteWS;
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;

@SuppressWarnings("unused")
@Controller
public class ProcesoBusquedaMultipleSW implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoBusquedaMultipleSW.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/procesoBusquedaMultipleSW", method = RequestMethod.POST)
	public ResponseEntity<String> procesa(@RequestBody String json) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;

		try {
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
			}.getType());
		} catch (Exception ex) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		if (map.get("valor") == null) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		try {
			/***********************************
			 * ORDEN DE BUSQUEDA 1) CREDITO PROCREA, 2) AHORRO PROCREA, 3)
			 * AHORRO CERO, 4) CREDITO CERO
			 ************************************/

			Map<String, String> mapCta = null;
			String tipoCta = "";
			RespuestaSVC respMult = ServiciosBusquedaAhoProcreaWS.procesa(map);
			if (respMult.getErrores().getCodigoError() != 0){
				if (map.containsKey("limite")) 
					respMult = ServiciosCuentasMultiplesCeroWS.leerCuentaMultipleAhorroCeroSW(map);
				// if(respMult.getErrores().getCodigoError() != 0) respMult =
				// ServiciosBusquedaCreProcreaWS.procesa(map);
				// if(respMult.getErrores().getCodigoError() != 0) respMult =
				// ServiciosBusquedaCreCeroWS.procesa(map);
			}
			if (respMult.getErrores().getCodigoError() != 0) {
				Respuesta tmpResp = ServiciosBusquedaComisionistaWS.procesa((String) map.get("valor"));
				if (tmpResp.getCodigo() != 0) {
					respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
							"[1]NO SE ENCUENTRA LA CUENTA EN EL SISTEMA");
				}else{
					tipoCta="AHOPOSOPC";
					respuestaSvc.getBody().addValor("CUENTA", (String) map.get("valor"));
					respuestaSvc.getBody().addValor("TIPO_CTA", tipoCta);
				}
			} else {
				tipoCta = ToolsR._T(respMult.getBody().getValor("TIPO_CTA"));
				if (tipoCta.equals("AHOCER")) {
					respuestaSvc = respMult;
				}else{
					mapCta = (Map<String, String>) respMult.getBody().getValor("CUENTA");
					RespuestaSVC respSol = ServiciosBusquedaSolicitanteWS
							.procesa(ToolsR._T(mapCta.get("SOLICITANTE_ID")));
					if (respSol.getErrores().getCodigoError() != 0) {
						respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
								respSol.getErrores().getDescError());
					} else {
						Map<String, String> mapSol = (Map<String, String>) respSol.getBody().getValor("SOLICITANTE");
						if (mapCta == null || mapSol == null) {
							respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
									"[2]NO SE ENCUENTRA LA CUENTA EN EL SISTEMA");
						} else {
							mapCta.putAll(mapSol);
							respuestaSvc.getBody().addValor("CUENTA", mapCta);
							respuestaSvc.getBody().addValor("TIPO_CTA", "AHOPRO");
						}
					}
				} 
			}

			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		} catch (DataAccessException ex) {
			log.error(ex.getMessage(),ex);
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		jsonResponse = gson.toJson(resp);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
}
