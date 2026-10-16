package net.cero.ahorro.logica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.*;
import net.cero.data.nuevospei.SolicitanteOBJ;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.spring.dao.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.excepcion.DaoException;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SolicitanteLogic {

	private static final Logger log = LogManager.getLogger(SolicitanteLogic.class);
	private static Apps apps = null;
	private static SolicitanteDAO sdao = null;
	private static PosicionGlobalDAO pdao = null;
	private static BloqueoDesbloqueoAppDao bdadao = null;
    private static AuditoriaDAO adao = null;
	private static DirectorioTelefonicoDAO dtdao = null;
	private static ColoniaDAO cdao = null;
	private static HistoricoDomiciliosDAO historicoDomiciliosDAO;

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
            adao = (AuditoriaDAO) s.getApplicationContext().getBean("AuditoriaDAO");
			dtdao = (DirectorioTelefonicoDAO) s.getApplicationContext().getBean("DirectorioTelefonicoDAO");
			cdao = (ColoniaDAO) s.getApplicationContext().getBean("ColoniaDAO");
			historicoDomiciliosDAO = (HistoricoDomiciliosDAO) s.getApplicationContext().getBean("HistoricoDomiciliosDAO");

			// gson = new Gson();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public Respuesta cuentaAmbienteReferencia(CuentaAmbienteReferenciaDTO body) throws DaoException {
		initialized();
		Gson gson = new Gson();
		Respuesta respuesta = new Respuesta();
		List<Map<String, Object>> rowCuentaAsp = new ArrayList<>();
		String cuentaAsp = "";
		String tipoCuenta;
		Respuesta res;

		try {
			//Se obtienen datos del solicitante
			Map<String, Object> referenciaSolicitante = sdao.referenciaCuentaAmbiente(body);

			//Se setean datos del solicitante
			CuentaAmbienteCabeceroResp cabeceroResp = loadCabeceros(referenciaSolicitante);
			final String numeroSolicitante = cabeceroResp.getNumero();

			if(Objects.isNull(body.getClaveProducto())) {
				return new Respuesta(4, "No se ha especificado el tipo de producto");
			}

			//Se busca por clave el producto en al base de datos
			List<Map<String, Object>> producto = sdao.consultaProducto(body.getClaveProducto());
			int idProducto = (int) producto.get(0).get("id_producto");

			if(producto.isEmpty()){
				return new Respuesta(4, "El producto proporcionado no se encuentra identificado");
			}

			if(Objects.nonNull(body.getCuentaASP()) || Objects.nonNull(body.getCuentaCLABE()) || Objects.nonNull(body.getNumeroTarjeta())){
				if (Objects.nonNull(body.getCuentaCLABE())){
					cuentaAsp = pdao.obtenerCuentaPorClabe(body.getCuentaCLABE());
				} else if(Objects.nonNull(body.getNumeroTarjeta())){
					cuentaAsp = pdao.obtenerCuentaPorTarjeta(body.getNumeroTarjeta());
				} else {
					cuentaAsp = body.getCuentaASP();
				}

				if (StringUtils.isBlank(cuentaAsp)){
					respuesta.setCodigo(6);
					respuesta.setMensaje("No se encontró la cuenta en la posición global");
					return respuesta;
				}

				tipoCuenta = pdao.obtenerPosicionGlobalCuenta(cuentaAsp);

				if (StringUtils.isBlank(tipoCuenta)){
					respuesta.setCodigo(7);
					respuesta.setMensaje("No se encontró la cuenta en la posición global");
					return respuesta;
				}

				String cuentaAspTemp = cuentaAsp;
				String productoTemp = body.getClaveProducto();
				if(sdao.isProductAccordingToCuentaAsp(cuentaAspTemp, productoTemp)){
					respuesta.setCodigo(4);
					respuesta.setMensaje("La cuenta no corresponde al producto");
					respuesta.setData("");

					return respuesta;
				}
			} else {
				//Buscamos la cuenta del producto seleccionado
				res = obtenerCuentaPorPersonaId(numeroSolicitante, body.getClaveProducto(), idProducto);

				log.info(gson.toJson(res));

				if (res.getCodigo() == 0){
					if (!StringUtils.isBlank(res.getData()))
						cuentaAsp = res.getData();

					tipoCuenta = pdao.obtenerPosicionGlobalCuenta(cuentaAsp);
				} else {
					return res;
				}
			}

			if(!cuentaAsp.trim().isEmpty() && tipoCuenta.equals("AHORRO")){
				idProducto = obtenerClaveProcrea(body.getClaveProducto());
				log.info("Entra a buscar medios de disposición como una cuenta de procrea");
				rowCuentaAsp = sdao.consultaAspCuenta(cabeceroResp, numeroSolicitante, idProducto, cuentaAsp);

				if(!rowCuentaAsp.isEmpty()){
					cuentaAsp = (String) rowCuentaAsp.get(0).get("cuenta");
				}

				//consultaCuentaMiAhorro(cabeceroResp, numeroSolicitante, rowCuentaAsp);

				respuesta.setCodigo(0);
				respuesta.setMensaje("Exito");
				respuesta.setData(gson.toJson(cabeceroResp));

			}else if(!cuentaAsp.trim().isEmpty()) {
				log.info("Entra a buscar medios de disposición como una cuenta de cero");

				List<Map<String, Object>> rowMap = sdao.consultaAspCuenta(cabeceroResp, numeroSolicitante, idProducto, cuentaAsp);
				if(!rowMap.isEmpty()){
					cuentaAsp = (String) rowMap.get(0).get("cuenta");

					sdao.consultaMediosAcceso(cabeceroResp, numeroSolicitante, idProducto, cuentaAsp);

					if(cabeceroResp.getMediosDisposición() == null || cabeceroResp.getMediosDisposición().isEmpty())
					{
						cabeceroResp.setMediosDisposición(
								cabeceroResp.getMediosDisposición().isEmpty() ? null : cabeceroResp.getMediosDisposición());
						respuesta.setCodigo(1);
						respuesta.setMensaje("No se encontr&oacute; informaci&oacute;n para mostrar");
						respuesta.setData(null);
					}
				}else{
					cabeceroResp = new CuentaAmbienteCabeceroResp();
					respuesta.setMensaje("No se ha encontrado información con el tipo de producto especificado");
					respuesta.setCodigo(4);
				}
			}


			if(!StringUtils.isBlank(cuentaAsp)) {

				if (tipoCuenta.equals("AHORRO")){
					obtenerSaldoProcrea(cuentaAsp, cabeceroResp);
				} else {
					consultaSaldoCuentaAsp(cuentaAsp, cabeceroResp);
				}
			}else {
				cabeceroResp.setSaldoCuenta(0d);
			}
			respuesta.setData(gson.toJson(cabeceroResp));
		} catch (Exception e) {
			log.error("Error al consultar los medios de disposici&oacute;n",e);
			respuesta.setCodigo(4);
			respuesta.setMensaje(e.getMessage());
		}

		return respuesta;
	}

	private void obtenerSaldoProcrea(String cuenta, CuentaAmbienteCabeceroResp cabeceroResp){
		SaldoAhorro saldoAhorro = new SaldoAhorro();

		Map<String, Object> bodyMap = new HashMap<>();
		Gson gson = new Gson();
		Respuesta res = new Respuesta();

		res = saldoAhorro.consultaSaldoAhorro(cuenta);
		if (res.getCodigo() == 0) {
			cabeceroResp.setSaldoCuenta(Double.valueOf(res.getData()));

		} else {
			cabeceroResp.setSaldoCuenta(0d);
		}


	}

	private void productAccordingToCuentaAsp(String cuentaAspTemp) {

	}



	private void consultaCuentaMiAhorro(CuentaAmbienteCabeceroResp cabeceroResp,
										String numeroSolicitante,
										List<Map<String, Object>> rowCuentaAsp) throws DaoException {

		if(rowCuentaAsp.isEmpty()){
			// TODO: 16/11/2023 validar que si no existe datos retornar lo necesario
		}else{
			List<Map<String, Object>> listaCuentasMiAhorro = sdao.consultaSaldoDisponibleMiAhorro(numeroSolicitante);

				for (Map<String, Object> item: listaCuentasMiAhorro) {
					MedioDisposicion medioMiAhorro = new MedioDisposicion();

					medioMiAhorro.setMedio(WS_UTIL.CLABE_FIELD);
					medioMiAhorro.setClave(item.get("cuenta_clabe").toString());
					medioMiAhorro.setTipoProducto("MI AHORRO");

					String estatus = item.get("estatus").toString();

					medioMiAhorro.setEstatus(estatus.equals("VIG") ? "VIGENTE" : estatus.equals("CAN") ? "CANCELADO": "ND");

					cabeceroResp.agregarMedio(medioMiAhorro);
				}
			}
	}

	private void consultaSaldoCuentaAsp(String cuenta, CuentaAmbienteCabeceroResp cabeceroResp) {
			Map<String, Object> bodyMap = new HashMap<>();
			Gson gson = new Gson();

			final String CUENTA_ASP = cuenta;

			bodyMap.put("cuenta", CUENTA_ASP);

			MediaType media = MediaType.parse("application/json; charset=utf-8");
			OkHttpClient cliente = new OkHttpClient();

			String auth = Credentials.basic("ASP", "a5p2017$");
			String url = ConstantesUtil.CONSULTA_SALDO_CUENTA;
			log.info("CONSULTA SALDO url: {}",url);
			String body = gson.toJson(bodyMap);
			log.info("CONSULTA SALDO body: {}",body);
			Request request = new Request.Builder().header("Authorization", auth).url(url)
					.post(okhttp3.RequestBody.create(media, body)).build();
			try {
				Response response = cliente.newCall(request).execute();
				final String respuesta = response.body().string();
				log.info(respuesta);
				final Respuesta respuestaObj = gson.fromJson(respuesta, Respuesta.class);
				final ConsultaSaldoRespuesta consultaSaldoRespuesta = gson.fromJson(respuestaObj.getData(),
						ConsultaSaldoRespuesta.class);

				log.info("Respuesta objeto:"+consultaSaldoRespuesta);
				if(consultaSaldoRespuesta.getBody() != null && consultaSaldoRespuesta.getBody().getParams() != null)
					cabeceroResp.setSaldoCuenta(
							Double.valueOf(consultaSaldoRespuesta.getBody().getParams().get("SALDO_REAL").toString()));
				else
					cabeceroResp.setSaldoCuenta(0d);
			} catch (IOException e) {
				log.error("error al al consultar el saldo ", e);
				cabeceroResp.setSaldoCuenta(0d);
			}
	}

	private CuentaAmbienteCabeceroResp loadCabeceros(Map<String, Object> referenciaSolicitante) throws DaoException {
		CuentaAmbienteCabeceroResp ambienteCabeceroResp = new CuentaAmbienteCabeceroResp();

		if (!referenciaSolicitante.isEmpty()) {
			ambienteCabeceroResp.setNumero((String) referenciaSolicitante.get("numero"));
			ambienteCabeceroResp.setNombreCompleto((String) referenciaSolicitante.get("nombre"));
			ambienteCabeceroResp.setDireccion((String) referenciaSolicitante.get("domicilio"));
			ambienteCabeceroResp.setCurp((String) referenciaSolicitante.get("curp"));

			ambienteCabeceroResp.setTelefono( (String) referenciaSolicitante.get("telefono"));
			ambienteCabeceroResp.setCorreoElectronico( (String) referenciaSolicitante.get("correo"));
			ambienteCabeceroResp.setSexo( (String) referenciaSolicitante.get("sexo"));
			ambienteCabeceroResp.setRFC( (String) referenciaSolicitante.get("RFC"));

			ambienteCabeceroResp.setFechaNacimiento(loadFechaNacimiento(ambienteCabeceroResp.getCurp()));

			final String nivelCuenta = sdao.nivelCuentaAmbiente(ambienteCabeceroResp.getNumero());
			ambienteCabeceroResp.setNivelCuenta(nivelCuenta);


			final String ocupacion = (referenciaSolicitante.get("ocupacion") == null ? "N/P" : referenciaSolicitante.get("ocupacion").toString());
			final String estadoNac = (referenciaSolicitante.get("estado_nacimiento") == null ? "N/P":referenciaSolicitante.get("estado_nacimiento").toString());

			if(Objects.nonNull(ocupacion)) {
				ambienteCabeceroResp.setOcupacion( sdao.consultaOcupacion(ocupacion));
			}

			if(Objects.nonNull(estadoNac)) {
				ambienteCabeceroResp.setLugarNacimiento(sdao.consultaEstadoNaciemiento(estadoNac));
			}

			loadBeneficiarioObject(ambienteCabeceroResp, (Map) referenciaSolicitante.get("beneficiarioMap"));

			return ambienteCabeceroResp;
		}
		throw new DaoException("No se encontr&oacute; ning&uacute;n medio de acceso con las referencias");
	}

	private void loadBeneficiarioObject(CuentaAmbienteCabeceroResp ambienteCabeceroResp, Map<String, Object> beneficiarioMap) {
		CuentaAmbienteCabeceroResp.Beneficiario beneficiario = new CuentaAmbienteCabeceroResp.Beneficiario();

		if(!beneficiarioMap.isEmpty()){
			beneficiario.setNombre(String.valueOf(beneficiarioMap.get("nombre_beneficiario")));
			beneficiario.setCalle(String.valueOf(beneficiarioMap.get("calle")));
			beneficiario.setNumExt(String.valueOf(beneficiarioMap.get("no_exterior")));
			beneficiario.setNumInt(String.valueOf(beneficiarioMap.get("no_interior")));
			beneficiario.setMunicipio(String.valueOf(beneficiarioMap.get("municipio")));
			beneficiario.setCP(String.valueOf(beneficiarioMap.get("cp")));
			beneficiario.setColonia(String.valueOf(beneficiarioMap.get("colonia")));
			beneficiario.setCiudad(String.valueOf(beneficiarioMap.get("ciudad")));
			beneficiario.setTelefono(String.valueOf(beneficiarioMap.get("numero_celular")));
			beneficiario.setEntidadfederativa(String.valueOf(beneficiarioMap.get("entidad_federativa")));
			beneficiario.setFechaNacimiento(String.valueOf(beneficiarioMap.get("fecha_nacimiento")));

			loadParentesco(beneficiario, Integer.parseInt(String.valueOf(beneficiarioMap.get("relacion_id"))));
		}
		ambienteCabeceroResp.setBeneficiario(beneficiario);
	}

	private void loadParentesco(CuentaAmbienteCabeceroResp.Beneficiario beneficiario, int relacionId) {
		String parentesco = sdao.obtieneParentesco(relacionId);
		beneficiario.setParentesco(parentesco);
	}

	public static String obtenerFechaNacCurp(String curp) {
		String fechaNac = "";
		Calendar c = Calendar.getInstance();
		String anio = 19 + curp.substring(4, 6);
		if ((c.get(Calendar.YEAR) - Integer.parseInt(anio)) > 117) {
			fechaNac = curp.substring(8, 10) + "/" + curp.substring(6, 8) + "/" + "20" + curp.substring(4, 6);
			// edad = calcularEdad(fechaNac);

		} else {
			fechaNac = curp.substring(8, 10) + "/" + curp.substring(6, 8) + "/" + "19" + curp.substring(4, 6);
			// edad = calcularEdad(fechaNac);

		}
		return fechaNac;
	}

	private static String loadFechaNacimiento(String fechaNacimiento) {
		final String fechaCurp = fechaNacimiento.substring(4, 10);
		final String anioString = fechaCurp.substring(0, 2);
		final String mes = fechaCurp.substring(2, 4);
		final String dia = fechaCurp.substring(4, 6);
		DateTimeFormatter TWO_YEAR_FORMATTER = new DateTimeFormatterBuilder()
				.appendValueReduced(ChronoField.YEAR, 2, 2, 1950).toFormatter();
		int year = Year.parse(anioString, TWO_YEAR_FORMATTER).getValue();
		return String.format("%d-%s-%s", year, mes, dia);
	}

	public static long calcularEdad(String fechaNac) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		long edad = 0;
		try {
			LocalDate fecha = LocalDate.parse(fechaNac, format);
			LocalDate hoy = LocalDate.now();
			edad = ChronoUnit.YEARS.between(fecha, hoy);
		} catch (Exception ex) {
			log.info("Error al calcular la edad");
		}
		return edad;
	}

	private Respuesta obtenerCuentaPorPersonaId(String personaId, String claveProducto, int productoId){
		Respuesta res = new Respuesta();
		Integer idProcrea;
		log.info("obtenerCuentaPorPersonaId personaId: " + personaId + " | claveProducto: " + claveProducto + " | productoId: " + productoId);
		List<Map<String, Object>> rows;
		log.info("Se busca la cuenta en cero");
		//Se busca la cuenta en cero por persona id y clave de producto
		rows = sdao.consultaCuentaPorProductoYSolicitante(personaId, claveProducto);
		//Se valida si tiene mas de una cuenta
		if (rows.size() > 1)
			return new Respuesta(21, "El cliente tiene mas de una cuenta " + claveProducto);
		//Se valida si no encontró cuentas y busca en procrea si es verdadero

		idProcrea = obtenerClaveProcrea(claveProducto);

		if (idProcrea != null)
			productoId = idProcrea;


		if (rows.size() == 0)
			rows = sdao.consultaCuentaPorProductoYSolicitanteProcrea(personaId, productoId);


		if (rows.size() > 1)
			return new Respuesta(22, "El cliente tiene mas de una cuenta " + claveProducto);

		log.info("rows: " + rows.size());
		/*if (rows.size() == 0)
			return new Respuesta(23, "No se encontraron cuentas del producto " + claveProducto);*/
		//Valida si encontró una cuenta
		if (rows.size() == 1){
			res.setCodigo(0);
			res.setMensaje("Exito");
			res.setData((String) rows.get(0).get("cuenta"));
		} else {
			res.setCodigo(0);
			res.setMensaje("Exito");
			res.setData("");
		}

		return res;
	}

	private Integer obtenerClaveProcrea(String claveCero){
		Integer idProcrea;
		switch (claveCero){
			case "FACIL" : idProcrea = 6;
			break;
			case "AHORRO_ASP": idProcrea = 2;
			break;
			case "AHORRO_ASP_2" : idProcrea = 2;
			break;
			default: idProcrea = null;
		}

		return idProcrea;
	}
	public Respuesta actualizarSolicitante(ActualizarSolicitanteDTO req) throws DaoException {
        initialized();
        Gson gson = new Gson();

        Boolean resultado;
        Respuesta res = new Respuesta();

        if (StringUtils.isBlank(req.getTelefono()))
            return new Respuesta(11, "Es necesario proporcionar el telefono", "");


        Map<String, Object> map = bdadao.consultaSolicitante(req.getTelefono());

        String personaId = (String) map.get("id_solicitante");

        Map<String, Object> datosSolicitante = sdao.obtenerCurpRfc(personaId);

        if (Objects.nonNull(req.getCurp())){
            resultado = sdao.actualizarCurp(req.getCurp(), personaId);
            if (resultado)
                res = new Respuesta(0, "El CURP se actualizó correctamente", "");
            else
                res = new Respuesta(21, "Error al actualizar el CURP", "");

            if (res.getCodigo() == 0)
                adao.registrarAuditoria(personaId, req.getUsuarioId(), "48", req.getIp(), (String) datosSolicitante.get("curp"), req.getCurp(), "Actualización de curp Iris");
        } else if (Objects.nonNull(req.getRfc())){
            resultado = sdao.actualizarRfc(req.getRfc(), personaId);
            if (resultado)
                res = new Respuesta(0, "El RFC se actualizó correctamente", "");
            else
                res = new Respuesta(22, "Error al actualizar el RFC", "");

            if (res.getCodigo() == 0)
                adao.registrarAuditoria(personaId, req.getUsuarioId(), "48", req.getIp(), (String) datosSolicitante.get("rfc"), req.getRfc(), "Actualización de rfc Iris");
        } else {
            res = new Respuesta(12, "Es necesario proporcionar el curp o rfc", "");
        }

        log.info("actualizarSolicitante Respuesta: " + gson.toJson(res));
        return res;
    }

	public Respuesta actualizarSolicitanteTelefono(ActualizarSolicitanteTelefonoReq req) throws DaoException {
		initialized();
		Gson gson = new Gson();

		Integer resultado;
		Respuesta res = new Respuesta();

		//Validaciones de parametros de entrada
		if (StringUtils.isBlank(req.getSolicitanteId()))
			return new Respuesta(1, "Falta parámetro: solicitanteId", "");

		if (StringUtils.isBlank(req.getTelefono()))
			return new Respuesta(1, "Falta parámetro: telefono", "");

		//Validacion de solicitante en directorio telefonico
		DirectorioTelefonicoOBJ directorioSolicitante = dtdao.obtenerTelfonoCelularCoDi(req.getSolicitanteId());
		if (req.getTelefono().equals(directorioSolicitante.getTelefono()))
			return new Respuesta(2, "El teléfono ingresado ya le pertenece al usuario", "");

		//Validacion de telefono ya registrado por uno o mas solicitantes
		List<String> registrosTelefono = dtdao.obtenerSolicitantePorTelefono(req.getTelefono());
		if (registrosTelefono.size() == 1)
			return new Respuesta(3, "El teléfono ya se encuentra registrado por otra persona", "");
		else if (registrosTelefono.size() > 1)
			return new Respuesta(4, "El teléfono ya se encuentra registrado por mas de una persona", "");

		//Actualizar telefono asp pago del solicitante
		resultado = dtdao.actualizaDirectorioTelefonico(req.getTelefono(), 9, directorioSolicitante.getIdDirectorioTelefonico());
		if (resultado > 0)
			res = new Respuesta(0, "El teléfono se actualizó correctamente", "");
		else
			res = new Respuesta(21, "Error al actualizar el teléfono", "");

		//Registrar en bitacora la actualización
		if (res.getCodigo() == 0)
			adao.registrarAuditoria(req.getSolicitanteId(), req.getUsuarioId(), ConceptosUtil.CAMBIO_TEL_CODI_CLV, req.getIp(), directorioSolicitante.getTelefono(), req.getTelefono(), "Actualización de teléfono Iris");


		log.info("actualizarSolicitanteTelefono Respuesta: " + gson.toJson(res));
		return res;
	}



	public Respuesta actualizarSolicitanteCorreo(ActualizarSolicitanteCorreoReq req) throws DaoException {
		initialized();
		Gson gson = new Gson();

		Integer resultado;
		Respuesta res = new Respuesta();

		//Validaciones de parametros de entrada
		if (StringUtils.isBlank(req.getSolicitanteId()))
			return new Respuesta(1, "Falta parámetro: solicitanteId", "");

		if (StringUtils.isBlank(req.getCorreo()))
			return new Respuesta(1, "Falta parámetro: correo", "");

		//Validacion de correo en solicitante
		String correoSolicitante = sdao.obtenerCorreo(req.getSolicitanteId());
		if (req.getCorreo().equals(correoSolicitante))
			return new Respuesta(2, "El correo ingresado ya le pertenece al cliente", "");

		//Validacion de registros de correo registrados en solicitante
		List<String> registrosCorreo = sdao.obtenerSolicitantePorCorreo(req.getCorreo());
		if (registrosCorreo.size() == 1)
			return new Respuesta(3, "El correo ya se encuentra registrado por otra persona", "");
		else if (registrosCorreo.size() > 1)
			return new Respuesta(4, "El correo ya se encuentra registrado por mas de una persona", "");

		//ACtualizar correo del solicitante
		resultado = sdao.actualizarCorreo(req.getCorreo(), req.getSolicitanteId());
		if (resultado > 0)
			res = new Respuesta(0, "El correo se actualizó correctamente", "");
		else
			res = new Respuesta(21, "Error al actualizar el correo", "");

		//Registrar en bitacora la actualización
		if (res.getCodigo() == 0)
			adao.registrarAuditoria(req.getSolicitanteId(), req.getUsuarioId(), ConceptosUtil.CAMBIO_CORREO_CLV, req.getIp(), correoSolicitante, req.getCorreo(), "Actualización de correo Iris");


		log.info("actualizarSolicitanteCorreo Respuesta: " + gson.toJson(res));
		return res;
	}
	public Respuesta getDomicilioSolicitante (String solicitanteId) {
		initialized();
		Respuesta respuesta = new Respuesta();
		Gson gson = new Gson();
		try{
			log.info("getDomicilioSolicitante try solicitante: " + solicitanteId);
			ActualizarSolicitanteDomicilioReq domicilioRegistrado = sdao.obtenerDomicilioSolicitante(solicitanteId);
				domicilioRegistrado.setColonia(cdao.obtenerDatosCpbyCodigoCp(String.valueOf(domicilioRegistrado.getColoniaId())));

			respuesta.setCodigo(0);
			respuesta.setMensaje("Éxito");
			respuesta.setMensaje(gson.toJson(domicilioRegistrado));
		} catch (Exception ex) {
			log.info("Ocurrio un error al traer domicilio: " +  ex);
			respuesta.setCodigo(-1);
			respuesta.setMensaje("Error");
			respuesta.setMensaje(null);
		}
		return respuesta;
	}
	public Respuesta actualizarSolicitanteDomicilio(ActualizarSolicitanteDomicilioReq req) {
		initialized();
		Gson gson = new Gson();

		Respuesta res;
		Map<String, String> errores = validarCampos(req);
		//Validaciones de parametros de entrada
		if (!errores.isEmpty())
			return new Respuesta(1, "Bad request", "Datos Faltante", errores);

		//Obtener id de colonia
		/*Long coloniaId = obtenerColonia(req.getCodigoPostal(), req.getColoniaNombre());*/


		//Obtener información de domicilio de la colonia nueva
		// List<Map<String, Object>> informacionDomicilio = sdao.buscarInfoDomicilioV2(coloniaId.intValue());

		ActualizarSolicitanteDomicilioReq domicilioRegistrado = sdao.obtenerDomicilioSolicitante(req.getSolicitanteId());
		log.info("Imprimiendo datos de request: " + req);

		String domicilioCompleto = req.getDomicilio() + ", Col " + req.getColonia().getColonia() + ", C.P "
				+ req.getColonia().getCp() + ", " + req.getColonia().getLocalidad() + ", "
				+ req.getColonia().getMunicipio() + ", " + req.getColonia().getEstado();
		String domicilioCompletoFormateado = domicilioCompleto.length() <= 100 ? domicilioCompleto
				: domicilioCompleto.substring(0, 100);


		if (!validarDiferenciasDomicilio(domicilioCompletoFormateado ,req, domicilioRegistrado))
			return new Respuesta(2, "El domicilio es el mismo registrado", "");
		else if (!guardarHistoricoDomicilio(req.getSolicitanteId())) {
			return new Respuesta(3, "No se puedo guardar historico de domicilio", "");
		}
		else {
			res = actualizarDatosSolicitante(domicilioCompletoFormateado, domicilioRegistrado, req);
			log.info("actualizarSolicitanteDomicilio Respuesta: " + gson.toJson(res));
			return res;
		}

	}
	private boolean guardarHistoricoDomicilio(String solicitanteId) {
		log.info("Guardando historico Domicilio...");
		return historicoDomiciliosDAO.InsertarHistorico(solicitanteId);
	}
	private boolean validarDiferenciasDomicilio(String domicilioCompletoFormateado,
												ActualizarSolicitanteDomicilioReq req,
												ActualizarSolicitanteDomicilioReq domRegistrado) {

		if (!Objects.equals(req.getColoniaId(), domRegistrado.getColoniaId())) {
			log.info("El campo getColoniaId es diferente");
			return true;
		};
		if (!Objects.equals(req.getObservaciones(), domRegistrado.getObservaciones())) {
			log.info("El campo  getObservaciones es diferente");
			return true;
		};

		if (!Objects.equals(req.getCatDomicilio1(), domRegistrado.getCatDomicilio1())) {
			log.info("El campo getCatDomicilio1 es diferente");
			return true;
		};
		if (!Objects.equals(req.getCatDomicilio2(), domRegistrado.getCatDomicilio2())) {
			log.info("El campo getCatDomicilio2 es diferente");
			return true;
		};
		if (!Objects.equals(req.getCatDomicilio3(), domRegistrado.getCatDomicilio3())) {
			log.info("El campo getCatDomicilio3 es diferente");
			return true;
		};
		if (!Objects.equals(req.getCatDomicilio4(), domRegistrado.getCatDomicilio4())) {
			log.info("El campo getCatDomicilio4 es diferente");
			return true;
		};
		if (!Objects.equals(req.getCatDomicilio5(), domRegistrado.getCatDomicilio5())) {
			log.info("El campo getCatDomicilio5 es diferente");
			return true;
		};

		if (!Objects.equals(req.getDescripcionDomicilio1(), domRegistrado.getDescripcionDomicilio1())) {
			log.info("El campo getDescripcionDomicilio1 es diferente");
			return true;
		};
		if (!Objects.equals(req.getDescripcionDomicilio2(), domRegistrado.getDescripcionDomicilio2())) {
			log.info("El campo getDescripcionDomicilio2 es diferente");
			return true;
		};
		if (!Objects.equals(req.getDescripcionDomicilio3(), domRegistrado.getDescripcionDomicilio3())) {
			log.info("El campo getDescripcionDomicilio3 es diferente");
			return true;
		};
		if (!Objects.equals(req.getDescripcionDomicilio4(), domRegistrado.getDescripcionDomicilio4())) {
			log.info("El campo getDescripcionDomicilio4 es diferente");
			return true;
		};
		if (!Objects.equals(req.getDescripcionDomicilio5(), domRegistrado.getDescripcionDomicilio5())) {
			log.info("El campo getDescripcionDomicilio5 es diferente");
			return true;
		};

		if (!Objects.equals(domicilioCompletoFormateado, domRegistrado.getDomicilio())) {
			log.info("El campo domicilioCompletoFormateado es diferente");
			return true;
		};
		log.info("No se detectaron diferencias");
		return false; // No se detectaron diferencias
	}


	private Respuesta actualizarDatosSolicitante(String domicilioCompletoFormateado,
												 ActualizarSolicitanteDomicilioReq domicilioRegistrado,
												 ActualizarSolicitanteDomicilioReq req){
		int resultadoActConcepto = 0;
		int resultadoActSolicitante;
		Respuesta res;
		//Obtener id de colonia registrada
		String coloniaIdRegistrado = sdao.obtenerConceptoDomicilio(req.getSolicitanteId());

		//Validación para proceder a registrar o actualizar el concepto de id de colonia
		if (StringUtils.isBlank(coloniaIdRegistrado) ){
			resultadoActConcepto = sdao.insertarConceptoColonia(req.getSolicitanteId(), req.getColonia().getCp());
		} else if (!coloniaIdRegistrado.equals(req.getColonia().getCp())){
			resultadoActConcepto = sdao.actualizarConceptoColoniaSolicitante(req.getColonia().getCp(),
					req.getSolicitanteId(), req.getUsuarioId());
		}
		else {
			log.info("El concepto es igual a la colonia y si hay registro por lo que no se actualiza ni inserta.");
		}
		//Actualización de domicilio del solicitante
		resultadoActSolicitante = sdao.actualizarDomicilioSolicitante(domicilioCompletoFormateado, req);


		//Registro de actualización de concepto de id de colonia en bitácora
		if (resultadoActConcepto > 0) {
			adao.registrarAuditoria(req.getSolicitanteId(), req.getUsuarioId(), ConceptosUtil.CAMBIO_COL_ID_CLV,
					req.getIp(), coloniaIdRegistrado, req.getColonia().getCp(),
					"Actualización de concepto de colonia Iris");
		}

		//Registro de actualización de domicilio del solicitante en bitácora
		if (resultadoActSolicitante > 0) {
			registrarAuditoriaLogic(domicilioCompletoFormateado, domicilioRegistrado, req);
			res = new Respuesta(0, "El domicilio se actualizó correctamente", "");
		}else {
			res = new Respuesta(21, "Error al actualizar el domicilio", "");
		}

		return res;
	}
	private void registrarAuditoriaLogic(String domicilioCompletoFormateado,
										 ActualizarSolicitanteDomicilioReq domicilioRegistrado,
										 ActualizarSolicitanteDomicilioReq req){
		//Se registra auditoria por cada campo actualizado
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getDomicilio()), domicilioCompletoFormateado);
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getUsuarioId()), String.valueOf(req.getUsuarioId()));
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getColoniaId()), String.valueOf(req.getColoniaId()));
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getCatDomicilio1()), String.valueOf(req.getCatDomicilio1()));
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getCatDomicilio2()), String.valueOf(req.getCatDomicilio2()));
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getCatDomicilio3()), String.valueOf(req.getCatDomicilio3()));
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getCatDomicilio4()), String.valueOf(req.getCatDomicilio4()));
		registrarAuditoria(req, String.valueOf(domicilioRegistrado.getCatDomicilio5()), String.valueOf(req.getCatDomicilio5()));
		registrarAuditoria(req, domicilioRegistrado.getDescripcionDomicilio1(), req.getDescripcionDomicilio1());
		registrarAuditoria(req, domicilioRegistrado.getDescripcionDomicilio2(), req.getDescripcionDomicilio2());
		registrarAuditoria(req, domicilioRegistrado.getDescripcionDomicilio3(), req.getDescripcionDomicilio3());
		registrarAuditoria(req, domicilioRegistrado.getDescripcionDomicilio4(), req.getDescripcionDomicilio4());
		registrarAuditoria(req, domicilioRegistrado.getDescripcionDomicilio5(), req.getDescripcionDomicilio5());
		registrarAuditoria(req, domicilioRegistrado.getObservaciones(), req.getObservaciones());
	}
	private void registrarAuditoria(ActualizarSolicitanteDomicilioReq datosRegistroRequest,
									String actual,
									String nuevo) {
		String bddData = normalize(actual);
		String newData = normalize(nuevo);

		if (!Objects.equals(bddData, newData)) {
			adao.registrarAuditoria(
					datosRegistroRequest.getSolicitanteId(),
					datosRegistroRequest.getUsuarioId(),
					ConceptosUtil.CAMBIO_DOMICILIO_CLV,
					datosRegistroRequest.getIp(),
					bddData,
					newData,
					"Actualización de domicilio Iris"
			);
		}
	}

	private String normalize(String s) {
		return (s == null) ? null : s.trim();
	}

	private Long obtenerColonia(String cp, String coloniaNombre) {
		coloniaNombre = coloniaNombre.replace("COL ", "");
		coloniaNombre = coloniaNombre.replace("CONJ HAB  ", "");
		log.info("# coloniaNombre :: " + coloniaNombre);
		log.info("# cp :: " + cp);

		Long colonia = cdao.obtenerColoniaByCpNombreV2(cp, coloniaNombre);
		if (colonia > 0) {
			return colonia;
		}
		log.info("#No encontro la colonia: " + coloniaNombre + " Con el codigo postal: " + cp);
		log.info("#Se procede a obtener la colonia centro del estado del cual es el cp proporcionado: " + cp);
		// Aguascalientes
		if (Integer.valueOf(cp) >= 20000 && Integer.valueOf(cp) <= 20997) {
			return 1L;
		}
		// Baja California
		else if (Integer.valueOf(cp) >= 21000 && Integer.valueOf(cp) <= 22997) {
			return 1353L;
		}
		// Baja California Sur
		else if (Integer.valueOf(cp) >= 23000 && Integer.valueOf(cp) <= 23997) {
			return 3715L;
		}
		// Campeche
		else if (Integer.valueOf(cp) >= 24000 && Integer.valueOf(cp) <= 24936) {
			return 4703L;
		}
		// Coahuila
		else if (Integer.valueOf(cp) >= 25540 && Integer.valueOf(cp) <= 27466) {
			return 8977L;
		}
		// Colima
		else if (Integer.valueOf(cp) >= 28000 && Integer.valueOf(cp) <= 28989) {
			return 9783L;
		}
		// Chiapas
		else if (Integer.valueOf(cp) >= 29000 && Integer.valueOf(cp) <= 30997) {
			return 10617L;
		}
		// Chihuahua
		else if (Integer.valueOf(cp) >= 31000 && Integer.valueOf(cp) <= 33997) {
			return 18268L;
		}
		// CDMX
		else if (Integer.valueOf(cp) >= 1000 && Integer.valueOf(cp) <= 16900) {
			return 28206L;
		}
		// Durango
		else if (Integer.valueOf(cp) >= 34000 && Integer.valueOf(cp) <= 35987) {
			return 29242L;
		}
		// Guanajuato
		else if (Integer.valueOf(cp) >= 36000 && Integer.valueOf(cp) <= 38997) {
			return 36244L;
		}
		// Guerrero
		else if (Integer.valueOf(cp) >= 39000 && Integer.valueOf(cp) <= 41997) {
			return 158879L;
		}
		// Hidalgo
		else if (Integer.valueOf(cp) >= 42000 && Integer.valueOf(cp) <= 43998) {
			return 51941L;
		}
		// Jalisco
		else if (Integer.valueOf(cp) >= 44100 && Integer.valueOf(cp) <= 49994) {
			return 58033L;
		}
		// México
		else if (Integer.valueOf(cp) >= 50000 && Integer.valueOf(cp) <= 57950) {
			return 63648L;
		}
		// Michoacan
		else if (Integer.valueOf(cp) >= 58000 && Integer.valueOf(cp) <= 61998) {
			return 71776L;
		}
		// Morelos
		else if (Integer.valueOf(cp) >= 62000 && Integer.valueOf(cp) <= 62996) {
			return 82044L;
		}
		// Nayarit
		else if (Integer.valueOf(cp) >= 63000 && Integer.valueOf(cp) <= 63996) {
			return 83762L;
		}
		// Nuevo Leon
		else if (Integer.valueOf(cp) >= 64000 && Integer.valueOf(cp) <= 67996) {
			return 85755L;
		}
		// Oaxaca
		else if (Integer.valueOf(cp) >= 68000 && Integer.valueOf(cp) <= 71998) {
			return 90582L;
		}
		// Puebla
		else if (Integer.valueOf(cp) >= 72000 && Integer.valueOf(cp) <= 75997) {
			return 96616L;
		}
		// Queretaro
		else if (Integer.valueOf(cp) >= 76000 && Integer.valueOf(cp) <= 76998) {
			return 101945L;
		}
		// Quintana Roo
		else if (Integer.valueOf(cp) >= 77000 && Integer.valueOf(cp) <= 77997) {
			return 105128L;
		}
		// San Luis Potosí
		else if (Integer.valueOf(cp) >= 78000 && Integer.valueOf(cp) <= 79997) {
			return 106356L;
		}
		// Sinaloa
		else if (Integer.valueOf(cp) >= 80000 && Integer.valueOf(cp) <= 82996) {
			return 112336L;
		}
		// Sonora
		else if (Integer.valueOf(cp) >= 83000 && Integer.valueOf(cp) <= 85994) {
			return 116520L;
		}
		// Tabasco
		else if (Integer.valueOf(cp) >= 86000 && Integer.valueOf(cp) <= 86998) {
			return 125606L;
		}
		// Tamaulipas
		else if (Integer.valueOf(cp) >= 87000 && Integer.valueOf(cp) <= 89970) {
			return 128250L;
		}
		// Tlaxcala
		else if (Integer.valueOf(cp) >= 90000 && Integer.valueOf(cp) <= 90990) {
			return 131526L;
		}
		// Veracruz
		else if (Integer.valueOf(cp) >= 91000 && Integer.valueOf(cp) <= 96998) {
			return 132987L;
		}
		// Yucatán
		else if (Integer.valueOf(cp) >= 97000 && Integer.valueOf(cp) <= 97990) {
			return 146071L;
		}
		// Zacatecas
		else if (Integer.valueOf(cp) >= 98000 && Integer.valueOf(cp) <= 99998) {
			return 147730L;
		}

		return 1L; // Por default regrese colonia centro con clave 1
	}

	private Map<String, String> validarCampos(ActualizarSolicitanteDomicilioReq req) {
		Map<String, String> errores = new HashMap<>();

		if (req.getUsuarioId() == null) {
			errores.put("usuarioId", "El campo usuarioId no puede estar vacío");
		}
		if (estaVacio(req.getSolicitanteId())) {
			errores.put("solicitanteId", "El campo solicitanteId no puede estar vacío");
		}
		if (estaVacio(req.getIp())) {
			errores.put("ip", "El campo ip no puede estar vacío");
		}
		if (estaVacio(req.getDomicilio())) {
			errores.put("domicilio", "El campo domicilio no puede estar vacío");
		}

		if (req.getColoniaId() == null) {
			errores.put("coloniaId", "El campo coloniaId no puede estar vacío");
		}

		/*if (estaVacio(req.getLocalidad())) {
			errores.put("localidad", "El campo localidad no puede estar vacío");
		}*/

		if (req.getCatDomicilio1() == null) {
			errores.put("catDomicilio1", "El campo catDomicilio1 no puede estar vacío");
		}
		if (req.getCatDomicilio2() == null) {
			errores.put("catDomicilio2", "El campo catDomicilio2 no puede estar vacío");
		}
		if (req.getCatDomicilio3() == null) {
			errores.put("catDomicilio3", "El campo catDomicilio3 no puede estar vacío");
		}
		if (req.getCatDomicilio4() == null) {
			errores.put("catDomicilio4", "El campo catDomicilio4 no puede estar vacío");
		}
		/*if (req.getCatDomicilio5() == null) {
			errores.put("catDomicilio5", "El campo catDomicilio5 no puede estar vacío");
		}*/

		if (estaVacio(req.getDescripcionDomicilio1())) {
			errores.put("descripcionDomicilio1", "El campo descripcionDomicilio1 no puede estar vacío");
		}
		if (estaVacio(req.getDescripcionDomicilio2())) {
			errores.put("descripcionDomicilio2", "El campo descripcionDomicilio2 no puede estar vacío");
		}
		if (estaVacio(req.getDescripcionDomicilio3())) {
			errores.put("descripcionDomicilio3", "El campo descripcionDomicilio3 no puede estar vacío");
		}
		if (estaVacio(req.getDescripcionDomicilio4())) {
			errores.put("descripcionDomicilio4", "El campo descripcionDomicilio4 no puede estar vacío");
		}
		if (req.getColonia() != null) {
			if (estaVacio(req.getColonia().getCp())) {
				errores.put("colonia.cp", "El campo CP de coloniaDTO no puede estar vacío");
			}
			if (estaVacio(req.getColonia().getColonia())) {
				errores.put("colonia.colonia", "El campo colonia de coloniaDTO no puede estar vacío");
			}
			if (estaVacio(req.getColonia().getLocalidad())) {
				errores.put("colonia.localidad", "El localidad CP de coloniaDTO no puede estar vacío");
			}
			if (estaVacio(req.getColonia().getMunicipio())) {
				errores.put("colonia.municipio", "El campo municipio de coloniaDTO no puede estar vacío");
			}
			if (estaVacio(req.getColonia().getEstado())) {
				errores.put("colonia.estado", "El campo estado de coloniaDTO no puede estar vacío");
			}
		} else {
			errores.put("coloniaDTO", "El campo coloniaDTO no puede ser nulo");
		}

		return errores;
	}

	private boolean estaVacio(String valor) {
		return valor == null || valor.trim().isEmpty();
	}
}
