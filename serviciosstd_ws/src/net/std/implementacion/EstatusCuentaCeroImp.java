package net.std.implementacion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.dao.AhorroStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.request.CanalesReq;


public class EstatusCuentaCeroImp implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(EstatusCuentaCeroImp.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;

	private static Boolean initialized() {
		Boolean valida = true;
		if(dao != null && daoAho == null) return valida;
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(dao == null || daoAho == null) valida = false;
		return valida;
	}
	
	@SuppressWarnings("unused")
	public static RespuestaSVC procesar(String clabe){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Map<String, String> mapResultado = new HashMap<>();
		String autorizacion = null;
		HeaderWS header;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		/* VALIDA LA CONEXION A LOS BEANS DAO */
		if(!initialized()) {
			return Comun.RespError(Errores.ERROR_SIN_CONEXION_BD, Errores.desc(Errores.ERROR_SIN_CONEXION_BD));
		}
		
		try{
			/* OBTIENE LOS DATOS COMPLETOS DE LA CUENTA */
			RespuestaSVC respCtaDep = daoAho.leerCuentaAhorroClabeDao(Comun._TX(clabe));
			if(respCtaDep.getErrores().getCodigoError() != 0){
				return Comun.RespError(Errores.ERROR_CUENTA, Errores.desc(Errores.ERROR_CUENTA, clabe));
			}
			
			CuentaOBJ ctaDeposito = (CuentaOBJ) respCtaDep.getBody().getValor("CUENTA");
			
			/* VERIFICA QUE LA CUENTA ESTE VIGENTE */
			if(!"VIG".equals(Comun._T(ctaDeposito.getEstatusClave()))){
				return Comun.RespError(Errores.ERROR_CUENTA_NO_ACTIVADA, Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA, String.format("%s - %s", clabe, Comun._T(ctaDeposito.getEstatus()))));
			}
			
			/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */ 
			if("BLOQUEADO".equals(Comun._T(ctaDeposito.getBloqueado()))){
				return Comun.RespError(Errores.ERROR_CUENTA_BLOQUEADA, Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, clabe));
			}
			
			/* VERIFICA QUE LA CUENTA NO TENGA BLOQUEO POR PARTE DEL PARTICIPANTE */ 
			RespuestaSVC respPermiso = dao.leerCanalStdDao(ctaDeposito.getCuenta(), Comun._T(Constantes.APLICATIVO_AHORRO), Comun._T(Constantes.TRX_CUENTAS_AHORRO));
			if(respPermiso.getErrores().getCodigoError() == 0){
				CanalesReq req = (CanalesReq)respPermiso.getBody().getValor("CANAL");
				if(req.getStatus()){
					return Comun.RespError(Errores.ERROR_CUENTA_BLOQUEADA_POR_CANAL, Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA_POR_CANAL, clabe));
				}
			}
		
			respuestaSvc.getBody().addValor("RESULTADO", "OK");
		}catch(Exception ex){
			ex.printStackTrace();
			return Comun.RespError(Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()));
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}
}

