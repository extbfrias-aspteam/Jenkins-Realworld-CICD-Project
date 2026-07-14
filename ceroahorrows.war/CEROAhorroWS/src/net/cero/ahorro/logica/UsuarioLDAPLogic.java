package net.cero.ahorro.logica;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import com.google.gson.Gson;

import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.CambioContrasenaLDAPResponse;
import net.cero.data.CambioContrasenaRequest;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.SecuredPassword;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.ContrasenaLDAPDAO;
import net.cero.spring.dao.excepcion.DaoException;

@Log4j2
public class UsuarioLDAPLogic {
	private static ContrasenaLDAPDAO contrasenaLDAPDAO;
	private static Apps apps = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}

			contrasenaLDAPDAO = (ContrasenaLDAPDAO) s.getApplicationContext().getBean("ContrasenaLDAPDAO");
		} catch (Exception e) {
			log.error("Error al obtener el bean ",e);
		}
	}
	
	public Respuesta cambioContrasenaLDAP(CambioContrasenaRequest cambioContrasenaRequest) {
		initialized();
		Respuesta respuesta = new Respuesta();
		try {
			contrasenaLDAPDAO.consultaUsuario(cambioContrasenaRequest.getUsuario());
			
			StringBuilder builder = new StringBuilder();
			Gson gson = new Gson();
			
			builder.append(ConstantesUtil.CAMBIO_CONTRASENA_LDAP);
			builder.append(cambioContrasenaRequest.getUsuario());
			builder.append("/");
			builder.append(SecuredPassword.getSecurePassword(cambioContrasenaRequest.getNuevaContrasena(), cambioContrasenaRequest.getUsuario()));
		
			String respuestaCambioContrasena = WS_UTIL.doPostRequest("", builder.toString());	
			CambioContrasenaLDAPResponse cambioContrasenaLDAPResponse = gson.fromJson(respuestaCambioContrasena, CambioContrasenaLDAPResponse.class);
			String codigoStatus = cambioContrasenaLDAPResponse.getCodeEstatus();
			log.info("Respuesta cambio contraseña LDAP "+cambioContrasenaLDAPResponse);
			respuesta.setCodigo(0);
			respuesta.setData("");
			respuesta.setMensaje(WS_UTIL.cargaMensageCambioPassLDAP(codigoStatus));
		} catch (IOException e) {
			log.error("Error al invocar el endpoint", e);
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("Error al cambiar la constraseña");
		} catch (DaoException e) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje(e.getMessage());
		}
		return respuesta;
	}
}
