package net.cero.ahorroCero.svc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;


@SuppressWarnings("unused")
@Controller
public class ProcesoLeerCuentasAhorroCeroSW implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoLeerCuentasAhorroCeroSW.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/procesoLeerCuentasAhorroCeroSW", method=RequestMethod.POST)
	public ResponseEntity<String> procesoLeerCuentasAhorroCeroSW(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
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
			List<Map<String, String>> lstCtasTot = null;
			
			RespuestaSVC respuestaCta = ServiciosCuentasMultiplesCeroWS.leerCuentaMultipleAhorroCeroSW(map);
			if(respuestaCta.getErrores().getCodigoError() == 0){
				List<Map<String, String>> list = (List<Map<String, String>>)respuestaCta.getBody().getValor("CUENTAS_CERO");
				if(list != null){
					for(Map<String, String> mapCta  : list){
						RespuestaSVC respuestaSol = ServiciosSolicitanteBasicoPrWS.leerSolicitanteBasicoPrSW(Collections.singletonMap("numero", ToolsR._T(mapCta.get("CLIENTE_ID"))));
						if(respuestaSol.getErrores().getCodigoError() == 0){
							if(!"1".equals(ToolsR._T(respuestaSol.getBody().getValor("BLOQUEADO")))){
								Map<String, String> mapTmp = new HashMap<>();
								mapTmp.putAll(mapCta);
								for (Map.Entry<String, Object> entry : (respuestaSol.getBody().getValores()).entrySet()) {
									mapTmp.put(entry.getKey(), entry.getValue() == null ? null : ToolsR._T(entry.getValue()));
								}
								if(lstCtasTot == null) lstCtasTot = new ArrayList<>();
								lstCtasTot.add(mapTmp);
							}
						}
					}
				}
			}
			
			if(lstCtasTot != null){
				respuestaSvc.getBody().addValor("CUENTAS_CERO", lstCtasTot);
			}else{
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
