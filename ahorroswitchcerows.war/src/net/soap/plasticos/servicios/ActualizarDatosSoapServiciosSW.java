/**
 * 
 */
package net.soap.plasticos.servicios;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.cero.plastico.actualizardatos.ActualizarDatosIfz;
import net.cero.plastico.actualizardatos.ActualizarDatosIfzService;
import net.cero.plastico.actualizardatos.ActualizarDatosRequest;
import net.cero.plastico.actualizardatos.ActualizarDatosResponse;
import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.ws.data.Constantes;
import net.cero.ws.data.Errores;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaErrorXML;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.RespuestaXML;

/**
 * @author ASP01A
 *
 */
public class ActualizarDatosSoapServiciosSW {
	private static final Logger log = LogManager.getLogger(ActualizarDatosSoapServiciosSW.class);

	public static RespuestaSVC ActualizarDatos(PlaHeaderWS header, DatosPlasticoREQ pla) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		URL wsdl = null;
		DatosPlasticoOBJ obj = null;

		try{
			wsdl = new URL(ActualizarDatosIfzService.WSDL_LOCATION.toString().replace("localhost", Constantes.SERVICIOS_SISCOOP));
		}catch(Exception ex){
			log.error(ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error al obtener el destino del servicio");;
			return respuestaSvc;
		}

		try{
			ActualizarDatosIfzService service1 = new ActualizarDatosIfzService(wsdl);
			ActualizarDatosIfz port1 = service1.getActualizarDatosIfzPort();

			ActualizarDatosRequest req = new ActualizarDatosRequest();
			req.setHeader(header);
			req.setDatosPlasticoREQ(pla);
			ActualizarDatosResponse response = port1.procesar(req);
			RespuestaXML respuesta = response.getReturn();
			if(response.getReturn().getErrores().getCodigoError() == 0){
				Gson gson = new Gson();
				obj = (DatosPlasticoOBJ)gson.fromJson(respuesta.getBody().obtenerParametro("DATOS_PLASTICO_OBJ"), DatosPlasticoOBJ.class);
				String resultado = (String)respuesta.getBody().obtenerParametro("RESULTADO");
				String respuestaXML = (String)respuesta.getBody().obtenerParametro("RESPUESTA_XML");
				respuestaSvc.getBody().addValor("DATOS_PLASTICO_OBJ", obj);
				log.info(String.format("%s\n%s", resultado, respuestaXML));
			}else{
				StringBuilder str = new StringBuilder();
				for(RespuestaErrorXML error : respuesta.getErrores().getErrores()){
					str.append(String.format("[%d] - %s\n",error.getCodigoError(), error.getDescError()));
				}
				respuesta.getErrores().addCodigo(-1, str);
			}
		}catch(Exception ex){
			log.error(ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error al ejecutar sevicio");;
			return respuestaSvc;
		}

		return respuestaSvc;
	}
}
