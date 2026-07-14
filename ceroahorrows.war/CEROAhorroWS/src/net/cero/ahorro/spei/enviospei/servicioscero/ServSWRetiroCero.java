package net.cero.ahorro.spei.enviospei.servicioscero;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.spei.enviospei.servicioscero.base.BaseServicioWS;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.ws.data.Errores;
import net.cero.req.general.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@Service
@AllArgsConstructor
public class ServSWRetiroCero extends BaseServicioWS {
	private final ClaveValorWS claveValorWS;
	public RespuestaSVC servSWRetiroCero(String cuenta, String cuentaD, Double monto, String fecha, String tipoMov, HeaderWS header, String observaciones) {
		String formaPago = "TRANSFERENCIA";
		if (tipoMov.equals("AH_PAGO_CRE"))
			formaPago = "TRASPASO AHORRO";
		RespuestaSVC respTraspaso = claveValorWS.getValorCatalogoWS(tipoMov,"claveValorTipoTransaccion");
		RespuestaSVC respEstatus = claveValorWS.getValorCatalogoWS("ALTA","claveValorEstatus");
		RespuestaSVC respFormaPago = claveValorWS.getValorCatalogoWS(formaPago,"claveValorFormaPago");
			

		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(ConstantesUtil.SWITCHER_WS).append("/").append("retirarAhorro").toString();
		log.info(uri);
		String jsonResponse;
		Respuesta resp = new Respuesta();

		if("".equals(ToolsR._T(cuenta)) || monto == null || "".equals(ToolsR._T(fecha))){
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "VALORES DE RETIRO INCORRECTO");
			return respuestaSvc;
		}
		
		try{

			String detalle = String.format("ORIGEN : %s, DESTINO : %s, MOVIMIENTO : %s, OBSERVACIONES : %s",
					ToolsR._T(cuenta),
					ToolsR._T(cuentaD),
					ToolsR._T(respTraspaso.getBody().getValor("DESCRIPCION")),
					ToolsR._T(observaciones));
			
			Map<String, Object> map = new HashMap<>();
			map.put("cuenta", ToolsR._T(cuenta));
			map.put("tipoTransaccionID", ToolsR._T(respTraspaso.getBody().getValor("ID"))); 
			map.put("tipoClave", tipoMov);
			map.put("descripcion", ToolsR._T(detalle));

			map.put("monto",  ToolsR._T(String.valueOf(monto)));
			map.put("estatusID", ToolsR._T(respEstatus.getBody().getValor("ID")));
			map.put("formaPagoID", ToolsR._T(respFormaPago.getBody().getValor("ID")));
			map.put("conciliado", "N");
			map.put("fecha", ToolsR._T(fecha));

			map.put("usuarioID", ToolsR._T(header.getIdUsuario()));
			map.put("host", ToolsR._T(header.getIpHost()));
			map.put("canalID", ToolsR._T(header.getIdCanalAtencion()));
			map.put("sucursalID", ToolsR._T(header.getIdSucursal()));

			jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
			log.info("Respuesta servSWRetiroCero: {}",jsonResponse);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ToolsR._T(resp.getMensaje()).toUpperCase());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "servSWRetiroCero");
		}
		return respuestaSvc;
	}
	
	public RespuestaSVC servSWRetiroCeroOperaciones(String cuenta, Double monto, String fecha, String tipoMov, HeaderWS header,
													String observaciones,String descripcion, Boolean cancelacion,
													Long banco_id, String clave_rastreo, String idSpei,String claveMovimientoDock) {
		RespuestaSVC respTraspaso = claveValorWS.getValorCatalogoWS(tipoMov,"claveValorTipoTransaccion");
		RespuestaSVC respFormaPago = claveValorWS.getValorCatalogoWS("EFECTIVO","claveValorFormaPago");
		RespuestaSVC respEstatus;
		if(cancelacion)
			respEstatus = claveValorWS.getValorCatalogoWS("CANC","claveValorAhorroEstatus");
		else
			respEstatus = claveValorWS.getValorCatalogoWS("ALTA","claveValorEstatus");

		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(ConstantesUtil.SWITCHER_WS).append("/").append("retirarAhorro").toString();
		log.info("servSWRetiroCeroOperaciones: {}"+uri);
		String jsonResponse;
		Respuesta resp = new Respuesta();

		if("".equals(ToolsR._T(cuenta)) || monto == null || "".equals(ToolsR._T(fecha))){
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "VALORES DE RETIRO INCORRECTO");
			return respuestaSvc;
		}
		
		try{

			Map<String, Object> map = new HashMap<>();
			map.put("cuenta", ToolsR._T(cuenta));
			map.put("tipoTransaccionID", ToolsR._T(respTraspaso.getBody().getValor("ID")));
			map.put("tipoClave", tipoMov);
			map.put("descripcion", ToolsR._T(descripcion));

			map.put("monto",  ToolsR._T(String.valueOf(monto)));
			map.put("estatusID", ToolsR._T(respEstatus.getBody().getValor("ID")));
			map.put("formaPagoID", ToolsR._T(respFormaPago.getBody().getValor("ID")));
			map.put("conciliado", "N");
			map.put("fecha", ToolsR._T(fecha));
			map.put("banco_id", ToolsR._T(banco_id));
			map.put("claveRastreo", ToolsR._T(clave_rastreo));
			map.put("idSpei", ToolsR._T(idSpei));

			map.put("usuarioID", ToolsR._T(header.getIdUsuario()));
			map.put("host", ToolsR._T(header.getIpHost()));
			map.put("canalID", ToolsR._T(header.getIdCanalAtencion()));
			map.put("sucursalID", ToolsR._T(header.getIdSucursal()));
			map.put("claveMovimientoDock", (!StringUtils.isBlank(claveMovimientoDock)? claveMovimientoDock : ""));

			log.info("Request: {}",map);
			jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
			log.info("Respuesta servSWRetiroCeroOperaciones: {}",jsonResponse);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ToolsR._T(resp.getMensaje()).toUpperCase());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "servSWRetiroCero");
		}
		return respuestaSvc;
	}
}
