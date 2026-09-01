package net.cero.ahorro.svc;
import java.io.Serializable;
import java.net.URL;

import javax.xml.ws.BindingProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.pagos.webservices.Pagos;
import org.jboss.pagos.webservices.PagosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;

import net.cero.ahorro.data.WSAspClientReq;
import net.cero.ahorro.data.WSAspClientRespuestaOBJ;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class ProcesoWSAspClientSW implements Serializable {
    private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoWSAspClientSW.class);

    @Autowired
	protected IPAuthenticationProvider authenticationManager;

    @PostMapping(value = "/WSAspClientPuente")
    public ResponseEntity<String> WSAspClientPuente(@RequestBody String json)
    {
		SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authenticate;
        ResponseEntity<String> response;
        Gson gson = new Gson();
        String muleResult = "";
		URL url;
		WSAspClientRespuestaOBJ respuestaObj = new WSAspClientRespuestaOBJ();
        WSAspClientReq objEntrada = new WSAspClientReq();
        authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		String resultadoJson;
		
        if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            return response;
        }
    	
		log.info("json entrada WSAspClientPuente : " + json);
        
        
		try {
			objEntrada = gson.fromJson(json, WSAspClientReq.class);
			if(objEntrada.getUrl() == null || objEntrada.getData() == null || objEntrada.getData().equals("") || objEntrada.getUrl().equals("")) {
				respuestaObj.setMsgHost("Faltan parametros obligatorios");
				respuestaObj.setResultado(2);
				resultadoJson = gson.toJson(respuestaObj);
				return new ResponseEntity<>(resultadoJson, HttpStatus.OK);
			}

			url = new URL(objEntrada.getUrl());
	        PagosService service1 = new PagosService(url);	        
	        Pagos port1 = service1.getPagosPort();	        

            ((BindingProvider)port1).getRequestContext().put("javax.xml.ws.client.connectionTimeout", 180000);
	        ((BindingProvider)port1).getRequestContext().put("javax.xml.ws.client.receiveTimeout", 180000);

	        String params = objEntrada.getData();
	        
	        muleResult = port1.aplicarPagos(params);
	        log.info("Resultado port1.aplicarPagos: " + muleResult);
  
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error WSAspClientPuente Message: " + e.getMessage() + ". CausedBy: " + e.getCause());
			respuestaObj.setMsgHost("Error WSAspClientPuente");
			respuestaObj.setResultado(2);
			resultadoJson = gson.toJson(respuestaObj);
			return new ResponseEntity<>(resultadoJson, HttpStatus.OK);
			
		}

        resultadoJson = gson.toJson(muleResult);
        return new ResponseEntity<>(muleResult, HttpStatus.OK);
    	
    }

}