package net.std.constantes;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.cero.ws.data.HeaderWS;

public class ValidaPermisos implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ValidaPermisos.class);

	public static Boolean valida(Long permisoTRX, Object ... args){
		Boolean correcto = true;
		
		log.info(String.format("VALIDANDO : %d - %s", permisoTRX, args.length == 0 ? "" : Comun._T(args[0])));
		
		try{
			CapaControlReqt controlReq = new CapaControlReqt();
			CapaControlResp controlResp;
			HeaderWS header = new HeaderWS();
			controlReq.setCuerpo(args == null || args.length == 0 ? "" : Comun._T(args[0]));
			header.setIdTransaccion(permisoTRX);
			controlReq.setHeader(header);

			SeCapaControl control = new SeCapaControl();
			controlResp = control.valida(controlReq);

			if(!controlResp.getCodeStatus().equals(SeControlEnum.SUCCESS.name())) {
				correcto = false;
			}
		}catch(Exception ex){
			correcto = false;
		}

		log.info(String.format("RESULTADO VALIDACION : %d - %s - %s", permisoTRX, 
				                                                      args.length == 0 ? "" : Comun._T(args[0]),
				                                                      correcto ? "OK" : "NO PASA"));
		return correcto;
	}
	
}


