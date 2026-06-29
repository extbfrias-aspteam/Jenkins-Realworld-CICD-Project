package net.std.soap.servicios;

import java.io.Serializable;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.cero.ws.data.Errores;
//import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.StdHeaderWS;
import net.cero.ws.data.ParametroBody;
import net.cero.ws.data.RespuestaSVC;
import net.std.clabe.soap.GeneraClabeIfz;
import net.std.clabe.soap.GeneraClabeIfzService;
import net.std.clabe.soap.GeneraClabeRequest;
import net.std.clabe.soap.GeneraClabeResponse;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;

public class ProcesoGeneraClabe implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoGeneraClabe.class);

	public static RespuestaSVC procesar(Integer productoId, String referencia, String aplicacion){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

			try {
				URL wsdl = new URL(GeneraClabeIfzService.WSDL_LOCATION.toString().replace("localhost", Constantes.MULE_WSSPEI));
				log.info(wsdl.toString());

				GeneraClabeIfzService service1 = new GeneraClabeIfzService(wsdl);
				GeneraClabeIfz port1 = service1.getGeneraClabeIfzPort();

				GeneraClabeRequest req = new GeneraClabeRequest();
				StdHeaderWS header = new StdHeaderWS();
				header.setIdEmpresa(Comun._L(Constantes.EMPRESA_ID));
				header.setIdUbicacion(Comun._L(Constantes.UBICACION_ID));

				req.setHeader(header);
				req.setProductoId(Comun._L(productoId));
				
				//req.setClaveAplicacion(Comun._T(Constantes.APLICACION_ID));   // <--- BLU = VERIFICAR: si se puede obtener por codigo
				req.setClaveAplicacion(aplicacion);   // <--- BLU = VERIFICAR: si se puede obtener por codigo
				
				
				req.setReferencia(referencia);
				req.setClaveRegion(Comun._T(Constantes.AGENTE_ID));
				req.setObtenerClabe(Comun._I(Constantes.OBTENER_CLABE_ID));

				GeneraClabeResponse response = port1.procesar(req);
				if (response.getReturn().getErrores().getCodigoError() == 0) {
					String clabe = null;
					for (ParametroBody res : response.getReturn().getBody().getParams()) {
						if (res.getNombre().equals("clabe")){
							clabe = res.getValor();
						}
					}
					respuestaSvc.getBody().addValor("CLABE", clabe);
				}else{
					respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, response.getReturn().getErrores().getDescError());
				}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuestaSvc;
	}
}

