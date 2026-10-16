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

import net.cero.ahorro.logica.DepositoAhorro;
import net.cero.data.DepositoAhorroReq;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class RegistraDepositoAhorro {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	private static DepositoAhorro depositoAhorro;
	Boolean continua = false;
	String msgValidacion = "";
	
	
	@RequestMapping(value = "/registraDepositoAhorro", method = RequestMethod.POST)
	public ResponseEntity<String> registraDepositoAhorro(@RequestBody String json) {
		depositoAhorro = new DepositoAhorro();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		DepositoAhorroReq req = gson.fromJson(json, DepositoAhorroReq.class);
		
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
	
	private void validaReq(DepositoAhorroReq req){
		continua = true;
		if (req.getHeader() == null ){
			continua = false;
			msgValidacion = "Header";
		}
		if(req.getCajaId() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Caja" : ", Caja");
		}
		if(req.getBancoId() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Banco" : ", Banco");
		}
		if(req.getCheque() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cheque" : ", Cheque");
		}
		if(req.getCuenta() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cuenta" : ", Cuenta");
		}
		if(req.getFecha() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Fecha" : ", Fecha");
		}
		if(req.getFormaPago() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Forma de pago" : ", Forma de pago");
		}
		if(req.getMonto() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Monto" : ", Monto");
		}
		if(req.getMovimientoId() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Movimiento" : ", Movimiento");
		}
		if(req.getHeader().getIdUsuario() <= 0L){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : ", Usuario");
		}
		
		if(req.getHeader().getUsuarioClave() == null || req.getHeader().getUsuarioClave().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : ", Usuario");
		}
	}
}

