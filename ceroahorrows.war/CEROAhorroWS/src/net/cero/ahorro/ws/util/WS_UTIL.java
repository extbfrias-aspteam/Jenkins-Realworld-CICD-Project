package net.cero.ahorro.ws.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;

import net.cero.data.DashboardCuentasOBJ;
import net.cero.seguridad.utilidades.SecuredPassword;
import net.cero.spring.dao.excepcion.DaoException;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Log4j2
public class WS_UTIL {
	
	public final static String MEDIO = "ASP";
	public final static String MEDIO_FIELD = "medio";
	public final static String CLABE_FIELD = "CLABE";
	public final static String CLAVE = "clave";
	public final static String TIPO = "tipo";
	public final static String STATUS = "estatus";
	public static final String TIPO_PRODUCTO = "tipoProducto";
	
	public static final String ENERO = "Enero";
	public static final String FEBRERO = "Febrero";
	public static final String MARZO = "Marzo";
	public static final String ABRIL = "Abril";
	public static final String MAYO = "Mayo";
	public static final String JUNIO = "Junio";
	public static final String JULIO = "Julio";
	public static final String AGOSTO = "Agosto";
	public static final String SEPTIEMBRE = "Septiembre";
	public static final String OCTUBRE = "Octubre";
	public static final String NOVIEMBRE = "Noviembre";
	public static final String DICIEMBRE = "Diciembre";
	
	public static final String CUENTA_VIGENTE ="VIGENTE";
	public static final String CUENTA_CANLEADA ="CANCELADA";
	public static final String CUENTA_BLOQUEDA="BLOQUEADA";
	public static final String ASP_BANCO = "ASP INTEGRA OPC";
	public static final String TARJETA_FISICA = "FISICA";
	public static final String TARJETA_VIRTUAL = "VIRTUAL";
	public static final int CODIGO_EXITO = 0;
	public static final int CODIGO_ERROR = 3;
	public static final String MOTIVO_BLOQUEO = "010";
	public static final String WHERE_SPEI_CVE_RASTREO = "cve_rastreo like '%s' ";
	public static final String WHERE_SPEI_OUT_CVE_RASTREO = "clave_rastreo like '%s' ";
	public static final String WHERE_SPEI_FECHA_CAPTURA = "(fecha_captura >= '%s 00:00:0.000' and fecha_captura <= '%s 23:59:00.000') ";
	public static final String WHERE_SPEI_OUT_FECHA_CAPTURA = "(fecha_captura >= '%s 00:00:0.000' and fecha_captura <= '%s 23:59:00.000') ";
	public static final String SPEI_IN = "IN";
	public static final String SPEI_OUT = "OUT";
	
	public static final int BLOQUEA_ASP_PAGO_APP = 1;
	public static final int DESBLOQUEA_ASP_PAGO_APP = 0;
	
	public static final String CAMBIO_CONTRASENA_LDAP_EXITOSO = "Cambio de Password Exitoso";
	public static final String CAMBIO_CONTRASENA_LDAP_EROR = "Ocurrió un error al cambiar el password, verifque que la contraseña sea diferente a las 5 anteriores usadas y que el usuario exista";

	public static final String FILTRO_CUENTA = "cuenta";
	public static final String FILTRO_PERSONA_ID = "persona_id";
	public static final int ID_CUENTA_FACIL = 6;
	public static final int ID_MI_AHORRO = 2;
	public static final String MENSAJE_FECHA_REGEX = "La fecha de operación no cumple con el formato YYYY-MM-DD";
	public static final String MENSAJE_FECHA = "No se ha proporcionado una fecha de operación";
	public static final String FECHA_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public static final String MENSAGE_NUM_REFERENCIA = "No se ha proprocionado un número de referencia";
	public static final String MENSAJE_PROVEEDOR = "No se ha proprocionado el nombre de un proveedor";
	public static final int INDEX_PROVEEDOR = 0;
	public static final int INDEX_REFERENCIA = 2;
	public static final int INDEX_ESTATUS = 3;
	public static final int INDEX_AUTORIZACION = 1;
	public static final int INDEX_MENSAJE_ESTATUS = 4;

	public static final String MENSAJE_FECHA_INICIO = "La fecha de inicio no ha sido proporcionado";
	public static final String FECHA_INICIO_REGEX = "La fecha de operación no cumple con el formato YYYY-MM-DD";;
	public static final String MENSAJE_FECHA_FIN = "La fecha final no ha sido proporcionado";;
	public static final int PAGO_NO_HISOTRICO = 5;
	public static final int PAGO_HISOTRICO = 4;
	public static final String PARAM_DESCRIPCION = " atc.descripcion ";
	public static final String PARAM_OBS = "mc.obs";
	public static final int INDEX_COMPANIA_RECARGA = 0;
	public static final int INDEX_TIPO_RECARGA=1;
	public static final int INDEX_NUMERO_TELEFONO_RECARGA=2;
	public static final int INDEX_AUTORIZACION_RECARGA=3;
	public static final int INDEX_ESTATUS_RECARGA=4;
	public static final int INDEX_MENSAJE_RECARGA=5;

	public static final int INDEX_ESTATUS_RECARGA_HISTORICO=3;
	public static final int RECARGA_NO_HISTORICO = 6;
	public static final int RECARGA_HISTORICO = 4;
	public static final String MENSAJE_CUENTA = "La cuenta ASP es requerida";
	public static final String CUENTA_AHORRO = "AHORRO";
	public static final String CUENTA_FACIL = "DEBITO";
	public static final String MENSAJE_HEADER = "El header es requerido";
	public static final Gson gson = new Gson();

    public static String doPostRequest(String body, String url) throws IOException{
		
		String respuesta = "";

		MediaType media = MediaType.parse("application/json; charset=utf-8");
		OkHttpClient cliente = new OkHttpClient();

		String auth = Credentials.basic("ASP", "a5p2017$");
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(329, TimeUnit.SECONDS)
				.build();

		Request request = new Request.Builder().header("Authorization", auth).url(url)
				.post(okhttp3.RequestBody.create(media, body)).build();

		Response response = client.newCall(request).execute();
		respuesta = response.body().string();
		return respuesta;
	}

	public static void construyeResponseDashboardCuentas(List<Map<String, Object>> totalEstatusMap,
			List<DashboardCuentasOBJ> dashboardCuentasOBJs, Map<String, Object> responseBodyMap) {

		Map<String , Object> conteoActual = totalEstatusMap.get(0);

		List<Map<String, Object>> cuentasEsperadas = Arrays.asList(getCuentasEsperadasMap("CANCELADA", conteoActual.get("cancaleada")),
																	getCuentasEsperadasMap("BLOQUEADA", conteoActual.get("bloqueada")),
																	getCuentasEsperadasMap("VIGENTE", conteoActual.get("vigente")));

		responseBodyMap.put("concentradoActual", cuentasEsperadas);
		responseBodyMap.put("detalladoAnual", dashboardCuentasOBJs);
	}

	public static Map<String, Object> getCuentasEsperadasMap(String estatus) {
		Map<String, Object> newMap = new HashMap<>();
		newMap.put("estatus", estatus);
		newMap.put("total", 0);

		return newMap;
	}
	public static Map<String, Object> getCuentasEsperadasMap(String estatus, Object conteo) {
		Map<String, Object> newMap = new HashMap<>();
		newMap.put("estatus", estatus);
		newMap.put("total", Objects.nonNull(conteo) ? conteo : 0);

		return newMap;
	}
	public static String CLAUSULA_WHERE_SPEI(String cveRastreo, String fecha, String tipoSpei) throws DaoException {
		String WHERE ="WHERE ";
		if(Objects.nonNull(cveRastreo)) {
			
			WHERE = WHERE.concat(String.format(tipoSpei.equals(SPEI_IN) ? WHERE_SPEI_CVE_RASTREO : WHERE_SPEI_OUT_CVE_RASTREO, cveRastreo));
			
			if(Objects.nonNull(fecha)) {
				WHERE = WHERE.concat(" and ").concat(cargaFiltroFechaSpei(fecha, tipoSpei));
			}
		}else if (Objects.nonNull(fecha)) {
			WHERE =WHERE.concat(cargaFiltroFechaSpei(fecha, tipoSpei));
		}else {
			return "";
		}
		
		return WHERE;
	}
	
	private static String cargaFiltroFechaSpei(String fecha, String tipoSpei) throws DaoException {
		if(tipoSpei.equals(SPEI_IN)) {
			return String.format(WHERE_SPEI_FECHA_CAPTURA, fecha, fecha);
		}
		
		return String.format(WHERE_SPEI_OUT_FECHA_CAPTURA, fecha, fecha);
	}
	
	public static String encryptPasswordLDAP(String password, String usuario) {
		return SecuredPassword.getSecurePassword(password,usuario);
	}

	public static String cargaMensageCambioPassLDAP(String codigoStatus) {
		Map<String, Object> mensajes = new HashMap<>();
		mensajes.put("SUCCESS", CAMBIO_CONTRASENA_LDAP_EXITOSO);
		mensajes.put("FAIL", CAMBIO_CONTRASENA_LDAP_EROR);
		return mensajes.get(codigoStatus).toString();
	}
}
