package net.cero.ahorro.ws;

import org.springframework.beans.factory.annotation.Autowired;
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

import net.cero.ahorro.logica.CajaDepositoAhorro;
import net.cero.ahorro.logica.DepositoAhorro;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.DepositoAhorroReq;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class CajaDepositoAhorroWS {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	private static CajaDepositoAhorro depositoAhorro;
	Boolean continua = false;
	String msgValidacion = "";
	
	
	@RequestMapping(value = "/cajaDepositoAhorro", method = RequestMethod.POST)
	public ResponseEntity<String> cajaDepositoAhorro(@RequestBody String json) {
		depositoAhorro = new CajaDepositoAhorro();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		CajaDepositoAhorroReq req = gson.fromJson(json, CajaDepositoAhorroReq.class);
		
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		try 
		{
			validaReq(req);
			if (!continua) {				
				resp.setCodigo(3);
				resp.setMensaje("Faltan datos obligatorios: " + msgValidacion);
			} else {
				resp = depositoAhorro.registrarDeposioto(req);
			}
		}
		catch(Exception e)
		{
			resp.setCodigo(-1);
			resp.setMensaje("Ocurrio un error interno al registrar el deposioto de ahorro:: "+ e.getMessage());			
		}
		
		jsonResponse = gson.toJson(resp);		
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		
		return response;
	}
	
	private void validaReq(CajaDepositoAhorroReq req){
		continua = true;
		if (req.getHeader() != null ){			
			if(req.getHeader().getIdUsuario() <= 0L){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : msgValidacion + ", Usuario");
			}
			
			if(req.getHeader().getUsuarioClave() == null || req.getHeader().getUsuarioClave().isEmpty()){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : msgValidacion + ", Usuario");
			}
		}else{
			continua = false;
			msgValidacion = "Header";
		}
		if(req.getCajaId() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Caja" : msgValidacion + ", Caja");
		}
		if(req.getBancoId() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Banco" : msgValidacion + ", Banco");
		}
		if(req.getCheque() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cheque" : msgValidacion + ", Cheque");
		}
		if(req.getCuentaAhorro() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cuenta" : msgValidacion + ", Cuenta");
		}
		if(req.getFecha() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Fecha" : msgValidacion + ", Fecha");
		}
		if(req.getFormaPago() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Forma de pago" : msgValidacion + ", Forma de pago");
		}
		if(req.getMonto() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Monto" : msgValidacion + ", Monto");
		}
		if(req.getMovtoId() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Movimiento" : msgValidacion + ", Movimiento");
		}
	}
}

