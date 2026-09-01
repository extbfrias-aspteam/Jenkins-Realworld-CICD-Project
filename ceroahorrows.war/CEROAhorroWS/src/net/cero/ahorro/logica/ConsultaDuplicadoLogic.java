package net.cero.ahorro.logica;

import com.google.gson.Gson;
import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.*;
import net.cero.data.seguridad.RespuestaSeg;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.*;
import net.cero.spring.dao.excepcion.DaoException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ConsultaDuplicadoLogic {

	private static final Logger log = LogManager.getLogger(ConsultaDuplicadoLogic.class);
	private static Apps apps = null;
	private static SolicitanteDAO sdao = null;
	private static PosicionGlobalDAO pdao = null;
	private static BloqueoDesbloqueoAppDao bdadao = null;
	private static DirectorioTelefonicoDAO dtdao = null;
	private static AhorroContratoDAO acdao = null;
	private static AhorroCuentasDAO acsdao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}

			sdao = (SolicitanteDAO) s.getApplicationContext().getBean("SolicitanteDAO");
			pdao = (PosicionGlobalDAO) s.getApplicationContext().getBean("PosicionGlobalDAO");
			bdadao = (BloqueoDesbloqueoAppDao) s.getApplicationContext().getBean("BloqueoDesbloqueoAppDao");
			dtdao = (DirectorioTelefonicoDAO) s.getApplicationContext().getBean("DirectorioTelefonicoDAO");
			acdao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			acsdao = (AhorroCuentasDAO) s.getApplicationContext().getBean("AhorroCuentasDAO");
			// gson = new Gson();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public RespuestaOBJ consultaDuplicado(ConsultaDuplicadoDTO body) throws DaoException {
		initialized();
		Gson gson = new Gson();
		RespuestaOBJ respuesta = new RespuestaOBJ();
		List<Map<String, Object>> rowCuentaAsp = new ArrayList<>();
		String cuentaAsp = "";
		String tipoCuenta;
		List<String> solicitantes;
		List<ConsultaDuplicatoRespDTO> data;

		respuesta = validarRequest(body);
		if (respuesta.getCodigo() != 0)
			return respuesta;

		try {
			if (Objects.nonNull(body.getTelefono())){
				solicitantes = dtdao.obtenerSolicitantePorTelefono(body.getTelefono());
			} else if (Objects.nonNull(body.getCorreo())) {
				solicitantes = sdao.obtenerSolicitantePorCorreo(body.getCorreo());
			} else {
				return new RespuestaOBJ(11, "No se proporcionaron datos de busqueda");
			}

			if (solicitantes.size() == 0){
				return new RespuestaOBJ(0, "No se encontraron registros");
			} else if(solicitantes.size() == 1){
				return new RespuestaOBJ(0, "No se encontraron registros repetidos");
			}

			data = obtenerDatos(solicitantes);
			respuesta.setCodigo(0);
			respuesta.setMensaje("Exito");
			respuesta.setData(data);
			//Se obtienen datos del solicitante

		} catch (Exception e) {
			log.error("Error al consultar los medios de disposici&oacute;n",e);
			e.printStackTrace();
			respuesta.setCodigo(4);
			respuesta.setMensaje(e.getMessage());
		}

		return respuesta;
	}


	private List<ConsultaDuplicatoRespDTO> obtenerDatos(List<String> solicitantes){
		List<ConsultaDuplicatoRespDTO> resp = new ArrayList<>();
		for (String solicitanteId : solicitantes){
			if (Objects.isNull(solicitanteId) || StringUtils.isBlank(solicitanteId)){
				ConsultaDuplicatoRespDTO solicitante = new ConsultaDuplicatoRespDTO();
				solicitante.setSolicitanteId("");
				solicitante.setCurp("");
				solicitante.setRfc("");
				solicitante.setNombre("");
				solicitante.setCorreo("");
				solicitante.setTelefono("");
				solicitante.setCuenta("");
				solicitante.setProducto("");
				resp.add(solicitante);
				continue;
			}

			DirectorioTelefonicoOBJ dt = dtdao.obtenerTelfonoCelularCoDi(solicitanteId);
			Map<String, Object> datos = sdao.obtenerCurpRfcPorSolicitanteId(solicitanteId);

			List<Map<String, Object>> cuentasProcrea = acdao.obtenerCuentasProcrea(solicitanteId);

			for (Map<String, Object> cuentaProcrea : cuentasProcrea){
				ConsultaDuplicatoRespDTO solicitante = new ConsultaDuplicatoRespDTO();
				solicitante.setSolicitanteId(solicitanteId);
				solicitante.setCurp(Objects.isNull((String) datos.get("curp")) ? "" : (String) datos.get("curp"));
				solicitante.setRfc(Objects.isNull((String) datos.get("rfc")) ? "" : (String) datos.get("rfc"));
				solicitante.setNombre(Objects.isNull((String) datos.get("nombre")) ? "" : (String) datos.get("nombre"));
				solicitante.setCorreo(Objects.isNull((String) datos.get("correo")) ? "" : (String) datos.get("correo"));
				solicitante.setTelefono(Objects.isNull(dt.getTelefono()) ? "" : dt.getTelefono());
				solicitante.setCuenta((String) cuentaProcrea.get("cuenta"));
				solicitante.setProducto((String) cuentaProcrea.get("producto"));
				resp.add(solicitante);
			}
			List<Map<String, Object>> cuentasCero = acsdao.obtenerCuentasCero(solicitanteId);

			for (Map<String, Object> cuentacero : cuentasCero){
				ConsultaDuplicatoRespDTO solicitante = new ConsultaDuplicatoRespDTO();
				solicitante.setSolicitanteId(solicitanteId);
				solicitante.setCurp(Objects.isNull((String) datos.get("curp")) ? "" : (String) datos.get("curp"));
				solicitante.setRfc(Objects.isNull((String) datos.get("rfc")) ? "" : (String) datos.get("rfc"));
				solicitante.setNombre(Objects.isNull((String) datos.get("nombre")) ? "" : (String) datos.get("nombre"));
				solicitante.setCorreo(Objects.isNull((String) datos.get("correo")) ? "" : (String) datos.get("correo"));
				solicitante.setTelefono(Objects.isNull(dt.getTelefono()) ? "" : dt.getTelefono());
				solicitante.setCuenta((String) cuentacero.get("cuenta"));
				solicitante.setProducto((String) cuentacero.get("producto"));
				resp.add(solicitante);

			}
			if (cuentasCero.isEmpty() && cuentasProcrea.isEmpty()){
				ConsultaDuplicatoRespDTO solicitante = new ConsultaDuplicatoRespDTO();
				solicitante.setSolicitanteId(solicitanteId);
				solicitante.setCurp(Objects.isNull((String) datos.get("curp")) ? "" : (String) datos.get("curp"));
				solicitante.setRfc(Objects.isNull((String) datos.get("rfc")) ? "" : (String) datos.get("rfc"));
				solicitante.setNombre(Objects.isNull((String) datos.get("nombre")) ? "" : (String) datos.get("nombre"));
				solicitante.setCorreo(Objects.isNull((String) datos.get("correo")) ? "" : (String) datos.get("correo"));
				solicitante.setTelefono(Objects.isNull(dt.getTelefono()) ?  "" : dt.getTelefono());
				solicitante.setCuenta("");
				solicitante.setProducto("");
				resp.add(solicitante);
			}


		}
		return resp;
	}

	private RespuestaOBJ validarRequest(ConsultaDuplicadoDTO req){
		String regexCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (Objects.isNull(req.getTelefono()) && Objects.isNull(req.getCorreo()))
			return new RespuestaOBJ(5, "Es necesario especificar un parámetros de entrada");

		if (Objects.nonNull(req.getTelefono())){
			if (StringUtils.isBlank(req.getTelefono()))
				return new RespuestaOBJ(5, "El número teléfonico no debe ser vacío");

			if (!req.getTelefono().matches("[0-9]{10}"))
				return new RespuestaOBJ(6, "El número telefónico debe tener solo números y ser de 10 dígitos");
		}
		if (Objects.nonNull(req.getCorreo())){
			if (StringUtils.isBlank(req.getCorreo()))
				return new RespuestaOBJ(5, "El correo no debe ser vacío");

			if (!req.getCorreo().matches(regexCorreo))
				return new RespuestaOBJ(6, "Formato de correo inválido");
		}


		return new RespuestaOBJ(0, "Exito");
	}



}
