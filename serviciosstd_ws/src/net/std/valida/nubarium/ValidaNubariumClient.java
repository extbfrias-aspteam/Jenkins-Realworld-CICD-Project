package net.std.valida.nubarium;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class ValidaNubariumClient {
	
	public String validarCurpNubarium(String curp) {
		String res = "OK";
		Map<String, String> respuesta = new HashMap<>();
		String codigo = "";

		try {
			ValidaNubariumService service = new ValidaNubariumService();

			ValidaNubarium port1 = service.getValidaNubariumPort();
			codigo = port1.validaCurp(curp);
			ObjectMapper mapper = new ObjectMapper();
			respuesta = mapper.readValue(codigo, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			res = "ERROR:: EN LA INVOCACION DEL WS validarCurpNubarium:" + e.getMessage();
			System.out.println(res);
			return res;
		}
		if (!respuesta.get("estatus").equals("OK")) {
			res = respuesta.get("mensaje");
		}
		return res;
	}
	
	public String validarRfcNubarium(String rfc) {
		String res = "OK";
		Map<String, String> respuesta = new HashMap<>();
		String codigo = "";

		try {
			ValidaNubariumService service = new ValidaNubariumService();

			ValidaNubarium port1 = service.getValidaNubariumPort();
			codigo = port1.validaRFC(rfc);
			ObjectMapper mapper = new ObjectMapper();
			respuesta = mapper.readValue(codigo, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			res = "ERROR:: EN LA INVOCACION DEL WS validarRfcNubarium:" + e.getMessage();
			System.out.println(res);
			return res;
		}
		if (!respuesta.get("estatus").equals("OK")) {
			res = respuesta.get("mensaje");
		}
		return res;
	}
}
