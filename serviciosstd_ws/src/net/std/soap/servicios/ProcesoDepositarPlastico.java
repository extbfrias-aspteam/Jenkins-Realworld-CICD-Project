package net.std.soap.servicios;

import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;

import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.plastico.depositar.DepositarIfz;
import net.cero.plastico.depositar.DepositarIfzService;
import net.cero.plastico.depositar.DepositarRequest;
import net.cero.plastico.depositar.DepositarResponse;
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
public class ProcesoDepositarPlastico {
	private static final Logger log = LogManager.getLogger(ProcesoDepositarPlastico.class);

	public static RespuestaSVC Depositar(PlaHeaderWS header, DatosPlasticoREQ pla) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		URL wsdl = null;
		DatosPlasticoOBJ obj = null;

		try{
			wsdl = new URL(DepositarIfzService.WSDL_LOCATION.toString().replace("localhost", Constantes.SERVICIOS_SISCOOP));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
			return respuestaSvc;
		}

		try{
			DepositarIfzService service1 = new DepositarIfzService(wsdl);
			DepositarIfz port1 = service1.getDepositarIfzPort();

			DepositarRequest req = new DepositarRequest();
			req.setHeader(header);
			req.setDatosPlasticoREQ(pla);
			DepositarResponse response = port1.procesar(req);
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
