package net.std.cuentas.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.std.constantes.Errores;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.request.TransaccionCuentasReq;

public class TransaccionSaldo {
	private static final Logger log = LogManager.getLogger(TransaccionSaldo.class);
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	public static RespuestaSVC transaccionLocal(CuentaOBJ cta, TransaccionCuentasReq trx, String tipoTransaccion, HeaderWS header, String autorizacion, Long idSpei, int movimiento){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_SIN_CONEXION_BD);
				return respuestaSvc;
			}
			
			
			respuestaSvc = dao.insertaTransaccionSaldo(cta, tipoTransaccion, trx.getMonto(), 
					trx.getConcepto(), autorizacion, header, trx.getClaveRastreo(), idSpei, movimiento);
			
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		return respuestaSvc;
	}
}
