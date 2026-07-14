package net.cero.ahorro.logica;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import net.cero.ahorro.spei.data.HeaderWS;
import net.cero.ahorro.spei.data.ParametroBody;
import net.cero.ahorro.spei.generaclabe.GeneraClabeIfz;
import net.cero.ahorro.spei.generaclabe.GeneraClabeIfzService;
import net.cero.ahorro.spei.generaclabe.GeneraClabeRequest;
import net.cero.ahorro.spei.generaclabe.GeneraClabeResponse;
import net.cero.spring.config.Apps;
import net.cero.spring.config.IPAuthenticationProvider;

@Log4j2
public class GenerarClabeLogic {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	public String generarClabe(String referencia, Integer productoId, String sucursalApertura) {
		String clabe = "";

		try {
			URL wsdl = new URL(GeneraClabeIfzService.WSDL_LOCATION.toString().replace("localhost",
					"sr-mule-sti.integraopciones.mx"));

			GeneraClabeIfzService service1 = new GeneraClabeIfzService(wsdl);
			GeneraClabeIfz port1 = service1.getGeneraClabeIfzPort();

			GeneraClabeRequest req = new GeneraClabeRequest();
			HeaderWS header = new HeaderWS();
			header.setIdEmpresa(1L);
			header.setIdUbicacion(1l);

			req.setHeader(header);
			req.setProductoId(Long.valueOf(productoId));
			req.setClaveAplicacion("8");
			req.setReferencia(referencia);
			req.setClaveRegion(sucursalApertura);
			req.setObtenerClabe(1);

			GeneraClabeResponse response = port1.procesar(req);

			if (response.getReturn().getErrores().getCodigoError() != 0) {
				log.info(
						String.format("%s: %s", response.getReturn().getErrores().getCodigoError(),
								response.getReturn().getErrores().getDescError()));
				
				//resp.setCodigo(Long.valueOf(response.getReturn().getErrores().getCodigoError()).intValue());
				//resp.setMensaje(response.getReturn().getErrores().getDescError());
				
				clabe = null;
			} else {
				for (ParametroBody res : response.getReturn().getBody().getParams()) {
					//log.info(res.getNombre() + ": " + res.getValor());
					if (res.getNombre().equals("clabe")){
						clabe = res.getValor();
					}else {
						clabe = null;
					}					
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		return clabe;
	}

}
