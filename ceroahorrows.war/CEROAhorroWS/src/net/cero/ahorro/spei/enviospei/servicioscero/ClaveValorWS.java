package net.cero.ahorro.spei.enviospei.servicioscero;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.spei.enviospei.servicioscero.base.BaseServicioWS;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@Service
public class ClaveValorWS extends BaseServicioWS {

	public RespuestaSVC getValorCatalogoWS(String clave,String recurso) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StringBuilder(ConstantesUtil.SIMPLIFICADA_WS).append("/").append(recurso).toString();
		log.info(uri);
		String jsonResponse;
		Respuesta resp = new Respuesta();
		Map<String, Object> map = new HashMap<>();
		map.put("clave", clave);

		try{
			jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, recurso);
		}
		return respuestaSvc;
	}
}
