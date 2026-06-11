package net.std.soap.servicios;

import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;

import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.plastico.retirar.RetirarIfz;
import net.cero.plastico.retirar.RetirarIfzService;
import net.cero.plastico.retirar.RetirarRequest;
import net.cero.plastico.retirar.RetirarResponse;
import net.cero.ws.data.Errores;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.RespuestaXML;
import net.std.constantes.Constantes;

/**
 * @author ASP01A
 *
 */
@SuppressWarnings("unused")
public class ProcesoRetirarPlastico {
	private static final Logger log = LogManager.getLogger(ProcesoRetirarPlastico.class);

	public static RespuestaSVC Retirar(PlaHeaderWS header, DatosPlasticoREQ pla) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		URL wsdl = null;
		DatosPlasticoOBJ obj = null;

		try{
			wsdl = new URL(RetirarIfzService.WSDL_LOCATION.toString().replace("localhost", Constantes.SERVICIOS_SISCOOP));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
			return respuestaSvc;
		}

		try{
			RetirarIfzService service1 = new RetirarIfzService(wsdl);
			RetirarIfz port1 = service1.getRetirarIfzPort();

			RetirarRequest req = new RetirarRequest();
			req.setHeader(header);
			req.setDatosPlasticoREQ(pla);
			RetirarResponse response = port1.procesar(req);
			RespuestaXML respuesta = response.getReturn();
			if(response.getReturn().getErrores().getCodigoError() == 0){
				Gson gson = new Gson();
				obj = (DatosPlasticoOBJ)gson.fromJson(respuesta.getBody().obtenerParametro("DATOS_PLASTICO_OBJ"), DatosPlasticoOBJ.class);
				respuestaSvc.getBody().addValor("DATOS_PLASTICO_OBJ", obj);
			}else{
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, respuesta.getErrores().getDescError());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());;
			return respuestaSvc;
		}

		return respuestaSvc;
	}
}
