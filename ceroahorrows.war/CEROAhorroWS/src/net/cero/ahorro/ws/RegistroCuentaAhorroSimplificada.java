package net.cero.ahorro.ws;

import lombok.extern.log4j.Log4j2;
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

import net.cero.ahorro.logica.RegistroCuentaAhorroSimplificadaLogic;
import net.cero.data.IneOcrRespOBJ;
import net.cero.data.RegistroCuentaAhorroSimplificadaReq;
import net.cero.data.ResponseService;
import net.cero.spring.config.IPAuthenticationProvider;

@Log4j2
@Controller
public class RegistroCuentaAhorroSimplificada {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	private static RegistroCuentaAhorroSimplificadaLogic registro;
	Boolean continua = false;
	String msgValidacion = "";
	
	
	@RequestMapping(value = "/registroCuentaAhorroSimplificada", method = RequestMethod.POST)
	public ResponseEntity<String> registroCuentaAhorroSimplificada(@RequestBody String json) {
		registro = new RegistroCuentaAhorroSimplificadaLogic();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		ResponseService resp = new ResponseService();
		//log.info("jsonRequest :: " + json);
		RegistroCuentaAhorroSimplificadaReq req = gson.fromJson(json, RegistroCuentaAhorroSimplificadaReq.class);
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		try 
		{
			validaReq(req);
			if (!continua) {				
				resp.setCode(3);
				resp.setMenssage("Faltan datos obligatorios: " + msgValidacion);
			} else {
				resp = registro.registroCuentaAhorroSimplificada(req);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMenssage("Ocurrio un error interno al registrar cuenta de ahorro:: "+ e.getMessage());			
		}
		
		jsonResponse = gson.toJson(resp);		
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		
		return response;
	}
	
	private void validaReq(RegistroCuentaAhorroSimplificadaReq req){
		continua = true;
		
		if(req.getIneOcr() == null) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "INE" : ", INE");
			
		}else if(req.getIneOcr().isEmpty()) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "INE" : ", INE");
		}
		
		Gson gson = new Gson();
		IneOcrRespOBJ ineOcer = new IneOcrRespOBJ();
		ineOcer = gson.fromJson(req.getIneOcr(), IneOcrRespOBJ.class);
		
		System.out.print("ineOcerNombre= "+ineOcer.getNombres());
		if(ineOcer.getNombres() == null) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Nombre" : ", Nombre");
			
		}else if(ineOcer.getNombres().isEmpty()) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Nombre" : ", Nombre");
		}
		
		if(ineOcer.getPrimerApellido() == null && ineOcer.getSegundoApellido() == null) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Apellidos" : ", Apellidos");
			
		}else if(ineOcer.getPrimerApellido().isEmpty() && ineOcer.getSegundoApellido().isEmpty()) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Apellidos" : ", Apellidos");
		}
		
		if(req.getCelular() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Celular" : ", Celular");
		}else if(req.getCelular().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Celular" : ", Celular");
		}
		
		if(req.getEmail() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Correo" : ", Correo");
		}else if(req.getEmail().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Correo" : ", Correo");
		}
		
		log.info("## Validación :: " + req.getValidarInfo());
		
		if(req.getValidarInfo() == null) {
			req.setValidarInfo(true);
		}
		//Verificar si se realizan estas validaciones o no
		if(req.getValidarInfo()) {
			if(req.getRfc() == null){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "RFC" : ", RFC");
			}else if(req.getRfc().isEmpty()) {
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "RFC" : ", RFC");
			}/*else if(req.getRfc().length() < 13 || req.getRfc().length() > 13) {
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "RFC invalido" : ", RFC invalido");
			}*/
			
			if(ineOcer.getCurp() == null){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "CURP" : ", CURP");
			}else if(ineOcer.getCurp().isEmpty()) {
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "CURP" : ", CURP");
			}/*else if(ineOcer.getCurp().length() < 18 || ineOcer.getCurp().length() > 18) {
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "CURP invalido" : ", CURP invalido");
			}*/
		}
		
		/*if(req.getPrimerNombre() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Primer nombre" : ", Primer nombre");
		}else if(req.getPrimerNombre().isEmpty()) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Primer nombre" : ", Primer nombre");
		}*/
		/*if(req.getSegundoNombre() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Segundo nombre" : ", Segundo nombre");
		}else if(req.getSegundoNombre().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Segundo nombre" : ", Segundo nombre");
		}*/
		/*if(req.getApellidoPaterno() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Apellido paterno" : ", Apellido paterno");
		}else if(req.getApellidoPaterno().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Apellido paterno" : ", Apellido paterno");
		}
		if(req.getApellidoMaterno() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Apellido materno" : ", Apellido materno");
		}else if(req.getApellidoMaterno().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Apellido materno" : ", Apellido materno");
		}*/
			
		/*if(req.getCurp() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "CURP" : ", CURP");
		}else if(req.getCurp().isEmpty()) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "CURP" : ", CURP");
		}else if(req.getCurp().length() < 18 || req.getCurp().length() > 18) {
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "CURP invalido" : ", CURP invalido");
		}*/
		
		/*if(req.getIne() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "INE" : ", INE");
		}else if(req.getIne().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "INE" : ", INE");
		}*/
		
		
	}
}

