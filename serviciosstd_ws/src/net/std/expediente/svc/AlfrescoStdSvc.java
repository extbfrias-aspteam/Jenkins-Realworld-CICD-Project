package net.std.expediente.svc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Errores;
import net.std.data.ExpedienteOBJ;
import net.std.servicios.ProcesoGeneraExpediente;

@SuppressWarnings("unused")
@Controller
public class AlfrescoStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AlfrescoStdSvc.class);
	
	public static final String  UPLOAD_FILE_PATH = "C:\\tmp\\ine.jpeg";
	public static final String FILE_TYPE = "image/jpeg";
	
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/alfrescoStd", method=RequestMethod.POST)
	public ResponseEntity<String> alfrescoStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
	
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		//String test = new Gson().fromJson(json, String.class);
		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

		try{
			String mensaje = "";
			File file = new File(UPLOAD_FILE_PATH);
            InputStream f = new FileInputStream(file);
            byte[] bytesArray = IOUtils.toByteArray(f);
            String imagen = IOUtils.toString(bytesArray);
            
            
			ExpedienteOBJ exp = new ExpedienteOBJ();
			exp.setCuentaId("73");
			exp.setCuenta("1120075609");
			exp.setDocumentosAhorroId("1");
			exp.setCve_Documento("COMP_DOM");
			//exp.setRutaAlfresco("/Alfresco/Personas/406908/Ahorro/1120075609");
			exp.setRutaAlfresco("Personas/406908/AhorroLineaBlu2/1120075609");
			exp.setIdArchivoAlfresco("1234567777888");
			exp.setObservaciones("test de prueba");
			//exp.setNombre(String.format("%s-%s-%s",Comun._T("test.txt"), "COMP_DOM", getVersion()));
			exp.setNombre(String.format("%s-%s",getVersion(), Comun._T("test.jpg")));
			exp.setFechaExpedicion("2019-01-01");
			exp.setFechaVigencia("2020-12-31");
			exp.setEstatusId("1");
			exp.setUsuarioId("9");
			exp.setImagen(imagen);
			exp.setPersonaId("406908");
			
			RespuestaSVC respAlfresco = ProcesoGeneraExpediente.procesar(exp);
			
			log.info(respAlfresco.getBody().getValor("RESULTADO"));

			mensaje += String.format("OK : %s | ", exp.getCve_Documento());
			respuestaSvc.getBody().addValor("RESULTADO", mensaje);
	
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	
	private String getVersion(){
		//return new SimpleDateFormat("yyyyMMddHHmmssZ").format(Calendar.getInstance().getTime());
		return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
	}
}

