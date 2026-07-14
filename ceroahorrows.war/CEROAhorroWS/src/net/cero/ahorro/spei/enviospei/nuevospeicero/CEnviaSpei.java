package net.cero.ahorro.spei.enviospei.nuevospeicero;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.ws.data.ToolsR;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class CEnviaSpei {

	private Integer id;
	private String descr;
	public void procesa(Map<String, Object> pojoSpei) {
		NuevoSpeiCeroResponse response = new NuevoSpeiCeroResponse();
		NuevoSpeiCeroRequest req;
		String sreturn;
		String token;

		try {
			id = -1;
			log.info("***********************");
			log.info("Create Web Service Client...");

			URL url = new URL(NuevoSpeiCeroIfzService.WSDL_LOCATION.toString().replace("localhost",
					ConstantesUtil.SERVICIOS_SPEI_OUT));
			log.info("URL: " +url.toString());
			NuevoSpeiCeroIfzService service1 = new NuevoSpeiCeroIfzService(url);
			log.info("Create Web Service...");
			log.info("RFC: " +pojoSpei);


			token = get_Token();
			
			req = new NuevoSpeiCeroRequest();
			req.setNombreOrdenante(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("NombreOrd"))));
			req.setCuentaOrdenante(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("CuentaOrd"))));
			req.setRfcOrdenante(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("RfcOrd"))));
			req.setTipoCuentaOrdenanteId(ToolsR._T(pojoSpei.get("IdTipoCuentaOrd")));
			req.setNombreBeneficiario(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("NombreBenef"))));
			req.setCuentaBeneficiario(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("CuentaBenef"))));
			req.setRfcBeneficiario(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("RfcBenef"))));
			req.setTipoCuentaBeneficiarioId(ToolsR._T(pojoSpei.get("IdTipoCuentaBenef")));
			req.setConceptoPago(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("ConceptoPago"))));
			req.setTipoPago(ToolsR._T(pojoSpei.get("IdTipoPago")));
			req.setMonto(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("Monto"))));
			req.setIva(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("Iva"))));
			req.setInstitucionBenId(ToolsR._I(pojoSpei.get("IdInstitucionBen")));
			req.setOperacionId(ToolsR._I(pojoSpei.get("IdOperacion")));
			req.setUsuarioId(ToolsR._L(pojoSpei.get("UsuarioId")));
			req.setAplicacion(ToolsR._I(pojoSpei.get("App")));
			req.setCorreoElectronico(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("CorreoOrd"))));
			req.setCorreoBeneficiario(CCifra.encodeBCDtoStr(ToolsR._T(pojoSpei.get("CorreoBenef"))));
			req.setToken(CCifra.encodeBCDtoStr(token));

			log.info("req: "+req);
			NuevoSpeiCeroIfz port1 = service1.getNuevoSpeiCeroIfzPort();
			response = port1.procesar(req);
			sreturn = response.getReturn();
			log.info("sreturn: "+response.getReturn());
			if (sreturn != null && !"".equals(sreturn)) {
				Map<String, String> datosMap = new Gson().fromJson(sreturn, new TypeToken<HashMap<String,  String>>() {}.getType());
				id = ToolsR._I(datosMap.get("code"));
				descr = ToolsR._T(datosMap.get("descripcion"));

				log.info("Respuesta CEnvioSPEI: "+datosMap);
			}

		} catch (Exception e) {
			descr = String.format("%s: %s", 1, "Error critico al enviar spei " + e.toString());
			log.error("Error critico al enviar spei ",e);
		}
	}
	private static String get_Token(){
		String token = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy|MM|dd|HH|mm|ss");
			token = sdf.format(Calendar.getInstance().getTime());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return token;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}

}

/** !CEnviaSpei.java */