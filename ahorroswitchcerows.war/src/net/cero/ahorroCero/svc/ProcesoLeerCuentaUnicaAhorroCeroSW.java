package net.cero.ahorroCero.svc;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.logging.log4j.LogManager;
import lombok.extern.log4j.Log4j2;
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
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;

@Log4j2
@Controller
public class ProcesoLeerCuentaUnicaAhorroCeroSW implements Serializable{
	private static final long serialVersionUID = 1L;
	//private static final Logger log = LogManager.getLogger(ProcesoLeerCuentaUnicaAhorroCeroSW.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/procesoLeerCuentaUnicaAhorroCeroSW", method=RequestMethod.POST)
	public ResponseEntity<String> procesoLeerCuentaUnicaAhorroCeroSW(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = null;
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;

		try{
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
		}catch(Exception ex){
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		try{
			RespuestaSVC respuestaCta = ServiciosCuentasMultiplesCeroWS.leerCuentaMultipleAhorroCeroSW(map);
			if(respuestaCta.getErrores().getCodigoError() == 0){
				List<Map<String, String>> list = (List<Map<String, String>>)respuestaCta.getBody().getValor("CUENTAS_CERO");
				if(list != null){
					respuestaSvc = new RespuestaSVC();
					RespuestaSVC respuestaSol = ServiciosSolicitanteBasicoPrWS.leerSolicitanteBasicoPrSW(Collections.singletonMap("numero", ToolsR._T(list.get(0).get("CLIENTE_ID"))));
					if(respuestaSol.getErrores().getCodigoError() == 0){
						if(!"1".equals(ToolsR._T(respuestaSol.getBody().getValor("BLOQUEADO")))){
							for (Map.Entry<String, String> entry : list.get(0).entrySet()) {
								respuestaSvc.getBody().addValor(entry.getKey(), entry.getValue() == null ? null : ToolsR._T(entry.getValue()));
							}
							for (Map.Entry<String, Object> entry : (respuestaSol.getBody().getValores()).entrySet()) {
								respuestaSvc.getBody().addValor(entry.getKey(), entry.getValue() == null ? null : ToolsR._T(entry.getValue()));
							}
						}else{
							respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "LA CUENTA ESTA EN ESTATUS DE BLOQUEADO CUENTA/CLIENTE");
						}
					}
				}
			}
			
			if(respuestaSvc == null){
				respuestaSvc = new RespuestaSVC();
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "NO SE ENCUENTRA CUENTAS EN EL SISTEMA");
			}
			
			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		}catch(DataAccessException ex){
			log.error(ex.getMessage(),ex);
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		jsonResponse = gson.toJson(resp);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
}
