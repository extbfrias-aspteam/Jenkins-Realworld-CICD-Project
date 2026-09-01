package net.cero.spring.dao;

import java.sql.SQLException;
import java.util.*;

import lombok.extern.log4j.Log4j2;
import net.cero.data.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.taskdefs.Echo;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.spring.dao.excepcion.DaoException;

@Log4j2
public class SolicitanteDAO{
	private JdbcTemplate jdbcTemplatePr;
	private JdbcTemplate jdbcTemplate;
	private String buscarSolicitanteByNumero;
	private String obtenerNombreSolicitanteByNumero;
	private String buscarSolicitenteExistenteByRfcCurp;
	private String sigSecSolicitante;
	private String nuevoSolicitante;
	private String nuevoSolicitanteV2;
	private String actualizaSolicitante;
	private String actualizaSolicitanteV2;
	private String obtenerEdoNacByCveCURP;
	private String registrarSolicitanteCuentaSimplificada;
	private String altaCanalAtencion;
	private String altaPais;
	private String buscarInfoSolicitanteF;
	private String buscarInfoSolicitanteM;
	private String obtenerNombrePais;
	private String obtenerCorreo;
	private String longitudSubirNivel;
	private String latitudSubirNivel;
	private String actualizaSolicitanteSubirNivelCuenta;
	private String altaConcepto;
	private String copiaDatosCeroProcrea;
	private String setDatosCeroProcrea;
	private String borraConceptoXIdSolIdCat;
	private String obtenerDomicilio;
	private String consultaPersonaId;
	private String consultaPersonaIdPorCuenta;
	private String consultaSolicitante;
	private String buscarCuentaPorPersona_id;
	private String consultaEntrega;
	private String validarCodigoAutorizacion;
	private String cambiarCodigoAutorizacion;

	private String cambiarCodigoAutorizacionBiometrico;
	private String insertarBitacoraCodigoAutorizacion;
	private String buscarSiExisteServicio;
	private String validarCodigoAutorizacionBiometrico;
	private String buscarIdColoniaPr;
	private String infoDomicilioPr;
	private String insertarConceptoSolicitanteBeneficiario;
	
	private String referenciaCuentaAmbiente;
	private String consultaMediosAcceso;
	private String nivelCuentaAmbiente;
	private String consultaDetalleCuenta;
	private String consultaAspCuentas;

	
	private String consultaEstadoNac;
	private String consultaOcupaciones;
	private String buscarClaveEstadoPorCurp;
	private String obtenerPais;
	private String guardarIdConsulta;
	private String concatenarDomicilio;

	private String altaPersonaF;
	private String altaPersonaM;
	private String buscarIdColonia;
	private String altaConIne;
	private String insertIdentificacion;
	private String buscarEstado;
	private String buscarSiExiste;
	private String buscarSolicitanteByCurp;
	private String buscarSolicitanteByRfc;
	private String buscarSiExisteMoral;
	private String infoDomicilio;
	private String altaSerieFirma;
	private String buscarIdEstado;
	private String getDocumentacionObligatoria;
	private String getDocumentacionCargada;
	private String obtenerRutaIne;
	private String consultaBeneficiario;
	private String consultaListaParentesco;
	private String consultaSaldoDisponibleMiAhorro;
	private String consultaCuentaPorProducto;
	private String consultaAspCuentasProcrea;
	private String consultaCuentaPorProductoYSolicitante;
	private String consultaCuentaPorProductoYSolicitanteProcrea;
	private String consultaCuentaPorProductoProcrea;
	private String obtenerSolicitantePorCorreo;
	private String obtenerCurpRfcPorSolicitanteId;
	private String actualizarCurp;
    private String actualizarRfc;
    private String obtenerCurpRfc;
	private String actualizarCorreo;
	private String obtenerConceptoDomicilio;
	private String infoDomicilioV2;
	private String insertarConceptoSolicitante;
	private String actualizarConceptoColoniaSolicitante;
	private String actualizarDomicilioSolicitante;
	private String obtenerDomicilioSolicitante;

	
	public Solicitante buscarSolicitanteByNumero(String solicitante) {
		
		Solicitante result = new Solicitante();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarSolicitanteByNumero, solicitante);
			if(!rows.isEmpty()) {
				result.setControl((String) rows.get(0).get("control"));
				result.setNombre((String) rows.get(0).get("nombre"));
				result.setRfc1((String) rows.get(0).get("rfc1"));
				result.setRfc2((String) rows.get(0).get("rfc2"));
				result.setRfc3((String) rows.get(0).get("rfc3"));
				result.setEdoCivil((String) rows.get(0).get("edo_civil"));
				result.setSexo((String) rows.get(0).get("sexo"));
				result.setDomicilio((String) rows.get(0).get("domicilio"));
				result.setColonia((Integer) rows.get(0).get("colonia"));
				result.setCelular((String) rows.get(0).get("celular"));
				result.setTelefono((String) rows.get(0).get("telefono"));
				result.setOriginario((String) rows.get(0).get("originario"));
				result.setEstudios((String) rows.get(0).get("estudios"));
				result.setAestudios((Integer) rows.get(0).get("aestudios"));
				result.setTestudios((String) rows.get(0).get("testudios"));
				result.setNombreP((String) rows.get(0).get("nombre_p"));
				result.setApellidos((String) rows.get(0).get("apellidos"));
				result.setNumero((String) rows.get(0).get("numero"));
				result.setNumeroCasa((String) rows.get(0).get("numero_casa"));
				result.setApellidoM((String) rows.get(0).get("apellido_m"));
				result.setCurp((String) rows.get(0).get("curp"));
				result.setCredencialIfe((String) rows.get(0).get("credencial_ife"));
				result.setTPersona((String) rows.get(0).get("t_persona"));
				result.setCveNacionalidad((Integer) rows.get(0).get("cve_nacionalidad"));
				result.setCveOcupacion((Integer) rows.get(0).get("cve_ocupacion"));
				result.setBloqueado((Integer) rows.get(0).get("bloqueado"));
				
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}

	public Solicitante buscarSolicitenteExistenteByRfcCurp(String rfc, String curp) {
		
		Solicitante result = new Solicitante();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarSolicitenteExistenteByRfcCurp, rfc, curp);

			if(!rows.isEmpty()) {
				result.setControl((String) rows.get(0).get("control"));
				result.setNombre((String) rows.get(0).get("nombre"));
				result.setRfc1((String) rows.get(0).get("rfc1"));
				result.setRfc2((String) rows.get(0).get("rfc2"));
				result.setRfc3((String) rows.get(0).get("rfc3"));
				result.setEdoCivil((String) rows.get(0).get("edo_civil"));
				result.setSexo((String) rows.get(0).get("sexo"));
				result.setDomicilio((String) rows.get(0).get("domicilio"));
				result.setColonia((Integer) rows.get(0).get("colonia"));
				result.setCelular((String) rows.get(0).get("celular"));
				result.setTelefono((String) rows.get(0).get("telefono"));
				result.setOriginario((String) rows.get(0).get("originario"));
				result.setEstudios((String) rows.get(0).get("estudios"));
				result.setAestudios((Integer) rows.get(0).get("aestudios"));
				result.setTestudios((String) rows.get(0).get("testudios"));
				result.setNombreP((String) rows.get(0).get("nombre_p"));
				result.setApellidos((String) rows.get(0).get("apellidos"));
				result.setNumero((String) rows.get(0).get("numero"));
				result.setNumeroCasa((String) rows.get(0).get("numero_casa"));
				result.setApellidoM((String) rows.get(0).get("apellido_m"));
				result.setCurp((String) rows.get(0).get("curp"));
				result.setCredencialIfe((String) rows.get(0).get("credencial_ife"));
				result.setTPersona((String) rows.get(0).get("t_persona"));
				result.setCveNacionalidad((Integer) rows.get(0).get("cve_nacionalidad"));
				result.setCveOcupacion((Integer) rows.get(0).get("cve_ocupacion"));
				result.setBloqueado((Integer) rows.get(0).get("bloqueado"));
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}

	private String sigSecSolicitante(){
		return jdbcTemplatePr.queryForObject(sigSecSolicitante, String.class);
	}
	
	
	public String nuevoSolicitante(Solicitante s) {
		try{
			s.setNumero(sigSecSolicitante());
			log.info("#numero de solicitante :: " + s.getNumero());
			jdbcTemplatePr.update(nuevoSolicitante,s.getNombre(),s.getNumero(),s.getNombreP(),s.getApellidos(),s.getApellidoM(),s.getDomicilio(),s.getColonia(),s.getCelular(),s.getTelefono(),s.getSexo(),s.getEdoCivil(),s.getRfc1(),s.getRfc2(),s.getRfc3(),s.getCurp(),s.getTPersona(),s.getCorreo(),s.getNumeroCasa(),s.getEdoNacId(),s.getCreadoPor());
		}catch(Exception e){
			log.info(e.getMessage());
			return null;
		}
		
		return s.getNumero();
	}
	
	public void actualizaSolicitante(Solicitante s) {
		try{
			jdbcTemplatePr.update(actualizaSolicitante,s.getCelular(),s.getCorreo(), s.getNumero());
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public Integer obtenerEdoNacByCveCURP(String curp) {
		try {
			return jdbcTemplatePr.queryForObject(obtenerEdoNacByCveCURP, new Object[]{curp}, Integer.class);			
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Boolean registrarSolicitanteCuentaSimplificada(String numeroSolicitante) {
		Boolean res;
		try{
			res = 0 > jdbcTemplatePr.update(registrarSolicitanteCuentaSimplificada, numeroSolicitante);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return res;
	}
	
	public Map<String, Object> referenciaCuentaAmbiente(CuentaAmbienteReferenciaDTO body) throws DaoException {
		Map<String, Object> map;
		List<Map<String, Object>> beneficiarioMap;
		final String whereBlock = " where ";
		final String filtro = obtieneFiltroUnico(body);

		try {
			map = jdbcTemplatePr.queryForMap(referenciaCuentaAmbiente.concat(whereBlock).concat(filtro));
		} catch (EmptyResultDataAccessException e) {
			log.error(e);
			throw new DaoException("No se encontr&oacute; ninguna referencia");
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new DaoException("Existen mas de una referencia para el dato proporcionado");
		}

		beneficiarioMap = jdbcTemplate.queryForList(consultaBeneficiario, String.valueOf(map.get("numero")));
		if(beneficiarioMap.isEmpty())
			map.put("beneficiarioMap", Collections.EMPTY_MAP);
		else
			map.put("beneficiarioMap", beneficiarioMap.get(0));

		return map;
	}

	public String obtieneParentesco(int parentescoId) {
		String parentesco;
		try {
			List<Map<String, Object>> parentescoList = jdbcTemplatePr.queryForList(consultaListaParentesco, parentescoId);

			if(!parentescoList.isEmpty())
				parentesco = parentescoList.get(0).get("descripcion").toString();
			else
				parentesco = "No identificado";

		} catch (EmptyResultDataAccessException e) {
			log.error(e);
			parentesco = "No reconocido";
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Error al consultar parentesco {}", e);
			parentesco = "No reconocido";
		}

		return parentesco;
	}
	private String obtieneFiltroUnico(CuentaAmbienteReferenciaDTO body) throws DaoException {
		String filtroPrimario = "";

		validaCriteriosBusqueda(body);
		if (Objects.nonNull(body.getRFC())) {
			filtroPrimario = String.format(" (s.rfc1 || s.rfc2 || s.rfc3 ) like '%s'", body.getRFC());
		}

		if (Objects.nonNull(body.getCURP())) {
			filtroPrimario = String.format("s.curp like '%s'", body.getCURP());
		}

		if (Objects.nonNull(body.getTelefono())) {
			filtroPrimario = String.format(" dt.telefono like '%s'", body.getTelefono());
		}

		if (Objects.nonNull(body.getCuentaASP())) {
			String filtro = "s.numero like '";
			String filtroFinal = " ";
			List<Map<String, Object>> referencia = this.consultaAspCuenta(body.getCuentaASP(), "cuenta");

			if (!referencia.isEmpty()) {
				filtroFinal = filtro + (String) referencia.get(0).get("persona_id") + "'";
			} else {
				throw new DaoException("No existe una cuenta asociada");
			}
			filtroPrimario = filtroFinal;
		}

		if (Objects.nonNull(body.getCuentaCLABE())) {
			String filtro = "s.numero like '";
			String filtroFinal = " ";
			List<Map<String, Object>> referencia = this.consultaAspCuenta(body.getCuentaCLABE(), "clabe_interbancaria");

			if (!referencia.isEmpty()) {
				filtroFinal = filtro + (String) referencia.get(0).get("persona_id") + "'";
			} else {
				throw new DaoException("No existe una referencia con la cuenta clable ");
			}
			filtroPrimario = filtroFinal;
		}

		if (Objects.nonNull(body.getNumeroTarjeta())) {
			String filtro = "s.numero like '";

			List<Map<String, Object>> referencia = this.consultaMediosAcceso(body.getNumeroTarjeta(), body, "pan");

			if (!referencia.isEmpty()) {
				filtroPrimario = filtro + (String) referencia.get(0).get("persona_id") + "'";
			} else {
				throw new DaoException("No existe una refere asociada con el numero de tarjeta");
			}
		}

		if (Objects.nonNull(body.getCorreo())) {
			filtroPrimario = String.format(" s.correo = '%s'", body.getCorreo());
		}

		return filtroPrimario;
	}
	
	public String consultaOcupacion(final String cveOcupacion) {
		try {
			Map<String, Object> row = this.jdbcTemplatePr.queryForMap(this.consultaOcupaciones, cveOcupacion);
			return row.get("descripcion").toString();
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
	}
	
	public String consultaEstadoNaciemiento(final String cveEstadoNacimiento) {
		try {
			Map<String, Object> row = this.jdbcTemplatePr.queryForMap(this.consultaEstadoNac, cveEstadoNacimiento);
			return row.get("nombre").toString();
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
	}

	private void validaCriteriosBusqueda(CuentaAmbienteReferenciaDTO body) throws DaoException {
		Set<String> camposBusqueda = new HashSet<>();

		if (Objects.nonNull(body.getRFC())) {
			camposBusqueda.add("rfc");
		}

		if (Objects.nonNull(body.getCURP())) {
			camposBusqueda.add("curp");
		}

		if (Objects.nonNull(body.getTelefono())) {
			camposBusqueda.add("telefono");
		}

		if (Objects.nonNull(body.getCuentaASP())) {
			camposBusqueda.add("cuentaASP");
		}

		if (Objects.nonNull(body.getCuentaCLABE())) {
			camposBusqueda.add("cuentaCLABE");
		}

		if (Objects.nonNull(body.getNumeroTarjeta())) {
			camposBusqueda.add("numero_tarjeta");
		}

		if (Objects.nonNull(body.getCorreo())) {
			camposBusqueda.add("numero_tarjeta");
		}

		if (camposBusqueda.size() > 1) {
			throw new DaoException("Se ha enviado más de un criterio de busqueda");
		} else if (camposBusqueda.size() == 0) {
			throw new DaoException("No se ha especificado el criterio de búsqueda");
		}
	}

	public List<Map<String, Object>> consultaMediosAcceso(final String value, CuentaAmbienteReferenciaDTO body,
			final String filterBy) throws DaoException {
		List<Map<String, Object>> mediosAcceso = new ArrayList<>();
		final String whereBlock = " where ".concat(filterBy).concat(" like '").concat(value).concat("'");

		try {
			mediosAcceso = jdbcTemplate.queryForList(consultaMediosAcceso.concat(whereBlock));
		} catch (DataAccessException e) {
			throw new DaoException("Error al consultar los medios de disposición");
		}

		return mediosAcceso;
	}
	
	public String consultaMediosAcceso(CuentaAmbienteCabeceroResp mediosDisposicionResp, final String personaId, int idProducto, String cuenta) throws DaoException {
		return consultaMediosAcceso(mediosDisposicionResp, personaId, true, idProducto, cuenta);
	}

	public String consultaMediosAcceso(CuentaAmbienteCabeceroResp mediosDisposicionResp, final String personaId, boolean enmascarado, int idProducto, String cuenta)
			throws DaoException {
		List<Map<String, Object>> mediosAcceso = new ArrayList<>();
		final String whereBlock = " where persona_id = '".concat(personaId).concat("' and nombre_proveedor like ?").concat(" and cuenta_asp = ?");
		String cuentaAsp = "";
		try {
			mediosAcceso = jdbcTemplate.queryForList(consultaMediosAcceso.concat(whereBlock), "MASTERCARD-CACAO", cuenta);

			if(!mediosAcceso.isEmpty()) {
				cuentaAsp = (String) mediosAcceso.get(0).get("cuenta_asp");
			}
			for (Map<String, Object> map : mediosAcceso) {
				MedioDisposicion mediosTarjetaFisicaODigital = new MedioDisposicion();

				final String MEDIO = "TAR";
				final String CARD_ID = map.get("card_id") != null ? map.get("card_id").toString() : "";
				final String CLAVE = (String) map.get("pan");
				final String TIPO = (String) map.get("tipo_tarjeta");
				final String ESTATUS = (String) map.get("estatus");
				final String TIPO_PRODUCTO = (String) map.get("tipo_producto");

				StringBuilder builder = new StringBuilder();

				if (enmascarado) {
					if (!StringUtils.isBlank(CLAVE) && CLAVE.length() > 6) {
						builder.append(StringUtils.repeat("*", 6));
						builder.append(CLAVE.substring(6));
					}
				} else {
					builder.append(CLAVE);
				}

				mediosTarjetaFisicaODigital.setMedio(MEDIO);
				mediosTarjetaFisicaODigital.setCardId(CARD_ID);
				mediosTarjetaFisicaODigital.setClave(builder.toString());
				mediosTarjetaFisicaODigital.setEstatus(ESTATUS);
				mediosTarjetaFisicaODigital.setTipoProducto(TIPO_PRODUCTO);
				mediosTarjetaFisicaODigital.setTipo(TIPO);

				mediosDisposicionResp.agregarMedio(mediosTarjetaFisicaODigital);
			}

		} catch (DataAccessException e) {
			throw new DaoException("Error al consultar los medios de disposición");
		}

		return cuentaAsp;
	}

	public Map<String, Object> consultaDetalleCuenta(final String value, final String filtarPor) throws DaoException {
		Map<String, Object> rowDetalle = new HashMap<>();

		try {
			final String WHERE = " where ".concat(filtarPor).concat("='").concat(value).concat("' ");
			rowDetalle = jdbcTemplate.queryForMap(consultaDetalleCuenta.concat(WHERE));
		} catch (DataAccessException e) {
			log.error(e);
			throw new DaoException("Error al obtener el detalle de las cuentas");
		}

		return rowDetalle;
	}

	public List<Map<String, Object>> consultaAspCuenta(final String value, String filterBy) throws DaoException {
		List<Map<String, Object>> row = new ArrayList<>();
		try {
			final String WHERE = " where ".concat(filterBy).concat(" = '").concat(value).concat("'");
			row = jdbcTemplate.queryForList(this.consultaAspCuentas.concat(WHERE));
		} catch (DataAccessException e) {

		}
		String filtro;
		if (row.isEmpty()) {
			if (filterBy.equals("clabe_interbancaria")){
				filtro = "cuenta_clabe";
			} else {
				filtro = "cuenta";
			}
			final String WHEREProcrea = " where ";
			String queryProcrea = "SELECT * FROM ahorro_contrato ac where " + filtro + " = ?";
			try {
				row = jdbcTemplatePr.queryForList(queryProcrea, value);

				row.get(0).put("persona_id", row.get(0).get("solicitante_id"));
				return row;
			}catch (DataAccessException ex) {
				throw new DaoException("No se obtuvo datos de la cuenta ASP");
			}
		}

		return row;
	}

	public List<Map<String, Object>> consultaAspCuenta(CuentaAmbienteCabeceroResp mediosDisposicionResp, final String value, int productoId, String cuenta)
			throws DaoException {
		List<Map<String, Object>> row = new ArrayList<>();
		String cuentaASP = "";
		try {
			final String WHERE = String.format(" where a.persona_id = '%s' AND a.producto_ahorro_id=%d AND a.cuenta = '%s'", value, productoId, cuenta);
			final String WHEREProcrea = String.format(" where ac.solicitante_id = '%s' AND ac.tipo_ahorro_id=%d AND ac.cuenta = '%s'", value, productoId, cuenta);
			row = jdbcTemplate.queryForList(this.consultaAspCuentas.concat(WHERE));
			if(!row.isEmpty()){
				cuentaASP = row.get(0).get("cuenta").toString();
			} else {
				row = jdbcTemplatePr.queryForList(this.consultaAspCuentasProcrea.concat(WHEREProcrea));
				if (!row.isEmpty()){
					cuentaASP = row.get(0).get("cuenta").toString();
				}
			}


			for (Map<String, Object> map : row) {
				MedioDisposicion medioDisposicion2 = new MedioDisposicion();
				
				final String cuentaAsp = (String) map.get("cuenta");
				final String estatus = (String) map.get("estatus");
				final String producto = (String) map.get("producto");

				medioDisposicion2.setMedio(WS_UTIL.MEDIO);
				medioDisposicion2.setClave(cuentaAsp);
				medioDisposicion2.setEstatus(estatus);
				medioDisposicion2.setTipoProducto(producto);
				medioDisposicion2.setTipo("");
				mediosDisposicionResp.agregarMedio(medioDisposicion2);
			}

			//if(productoId == WS_UTIL.ID_CUENTA_FACIL){
				for (Map<String, Object> map : row) {
					final String CLABE_INTERBANCARIA = (String) map.get("clabe_interbancaria");
					if (!CLABE_INTERBANCARIA.isEmpty()) {
						MedioDisposicion medioDisposicion2 = new MedioDisposicion();

						final String estatus = (String) map.get("estatus");
						final String producto = (String) map.get("producto");

						medioDisposicion2.setMedio(WS_UTIL.CLABE_FIELD);
						medioDisposicion2.setClave(CLABE_INTERBANCARIA);
						medioDisposicion2.setEstatus(estatus);
						medioDisposicion2.setTipoProducto(producto);
						medioDisposicion2.setTipo("");

						mediosDisposicionResp.agregarMedio(medioDisposicion2);
					}
				}
			//}

		} catch (DataAccessException e) {
			throw new DaoException("No se obtuvo datos de la cuenta ASP");
		}


		return row;
	}
	
	public String nivelCuentaAmbiente(String personaId) {
		Map<String, Object> rows;

		try {
			rows = jdbcTemplate.queryForMap(nivelCuentaAmbiente, personaId);

			if (!rows.isEmpty()) {
				return (String) rows.get("nivel");
			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error al obtener el nivel de la cuenta");
		}

		return "S/N";
	}

	public List<Map<String, Object>> consultaSaldoDisponibleMiAhorro(String personaId) throws DaoException {
		try {
			return jdbcTemplate.queryForList(this.consultaSaldoDisponibleMiAhorro, personaId);
		}catch (DataAccessException e){
			log.error("Error al ejecutar la consulta de saldo mi ahorro {}", e);
			throw new DaoException("Error al consultar el saldo de mi ahorro");
		}
	}

	public List<Map<String, Object>> consultaProducto(String producto) throws DaoException {
		try {
			final String query = String.format("select id id_producto, clave, descripcion  " +
					"from productos.prproductos_ahorro p" +
					" where p.clave like '%s'", producto);
			return jdbcTemplate.queryForList(query);
		} catch (DataAccessException e) {
			log.error("error al consultar los datos ",e);
			throw new DaoException("No se encontró datos de los productos");
		}
	}

	/**
	 *
	 * @param personaId
	 * @param claveProducto
	 * @return
	 */
	public List<Map<String, Object>> consultaCuentaPorProductoYSolicitante(String personaId, String claveProducto){
		String cuenta = "";
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		try {
			rows = jdbcTemplate.queryForList(consultaCuentaPorProductoYSolicitante, personaId, claveProducto);

		} catch(Exception e){
			e.printStackTrace();
			log.info("error al obtener cuenta en cero por producto y solicitante");
			return null;
		}


		return rows;
	}

	/**
	 *
	 * @param personaId
	 * @param productoId
	 * @return
	 */
	public List<Map<String, Object>> consultaCuentaPorProductoYSolicitanteProcrea(String personaId, int productoId){
		String cuenta = "";
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		try {
			rows = jdbcTemplatePr.queryForList(consultaCuentaPorProductoYSolicitanteProcrea, personaId, productoId);

		} catch(Exception e){
			log.info("error al obtener cuenta en cero por producto y solicitante");
			return null;
		}


		return rows;
	}


	public boolean isProductAccordingToCuentaAsp(String cuentaAsp, String claveProducto) throws DaoException {
		try{

			List<Map<String, Object>> obj = jdbcTemplate.queryForList(consultaCuentaPorProducto, claveProducto, cuentaAsp);
			log.info("claveProducto: " + claveProducto);
			if (obj.isEmpty()) {
				obj = jdbcTemplatePr.queryForList(consultaCuentaPorProductoProcrea, cuentaAsp);
				if (obj.isEmpty()){
					return true;
				}
			}
			return false;
		}catch (DataAccessException sq) {
			log.error("Error al consultar la cuenta respecto al producto", sq);
			throw new DaoException("Error al consultar la cuenta respecto al producto");
		}
	}
	public List<String> obtenerSolicitantePorCorreo(String correo) {
		List<String> solicitantes = new ArrayList<String>();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerSolicitantePorCorreo, correo);

			for (Map<String, Object> row : rows){
				solicitantes.add((String) row.get("id_solicitante"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			//log.info(e.getMessage());
		}

		return solicitantes;
	}

	public Map<String, Object> obtenerCurpRfcPorSolicitanteId(String solicitanteId) {
		Map<String, Object> row = new HashMap<>();

		try {
			row = jdbcTemplatePr.queryForMap(obtenerCurpRfcPorSolicitanteId, solicitanteId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());

			//log.info(e.getMessage());
		}

		return row;
	}

	public Boolean actualizarRfc(String rfc, String personaId) {
        Boolean res = true;;

        try{
            jdbcTemplatePr.update(actualizarRfc, rfc.substring(0, 4), rfc.substring(4, 10), rfc.substring(10, 13), personaId);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    public Boolean actualizarCurp(String curp, String personaId) {
        Boolean res = true;

        try{
            jdbcTemplatePr.update(actualizarCurp, curp, personaId);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    public Map<String, Object> obtenerCurpRfc(String personaId){
        String cuenta = "";
        Map<String, Object> row = new HashMap<String, Object>();

        try {
            row = jdbcTemplatePr.queryForMap(obtenerCurpRfc, personaId);

        } catch(Exception e){
            log.error("error en obtenerCurpRfc: " + e.getStackTrace());
            return null;
        }


        return row;
    }

	public String obtenerCorreo(String solicitanteId){
		String correo = null;
		Map<String, Object> row = new HashMap<String, Object>();

		try {
			row = jdbcTemplatePr.queryForMap(obtenerCorreo, solicitanteId);
			correo = (String) row.get("correo");

		} catch(Exception e){
			log.error("error en obtenerCorreo: " + e.getStackTrace());
			return null;
		}


		return correo;
	}



	public int actualizarCorreo(String correo, String solicitanteId) {
		Integer res = 0;
		try {

			res = jdbcTemplatePr.update(actualizarCorreo,correo, solicitanteId);

		}catch(Exception e) {

			e.printStackTrace();
		}
		return res;
	}

	public String obtenerConceptoDomicilio(String personaId) {
		String coloniaId = "";
		log.info("Entra a obtener concepto Domicilio");
		Map<String, Object> row = new HashMap<String, Object>();
		try {
			row = jdbcTemplatePr.queryForMap(obtenerConceptoDomicilio, personaId);
			coloniaId = (String) row.get("valor");
			if (coloniaId.equals(""))
				log.info("No encontro concepto Domicilio");
			else
				log.info("Si encontro concepto Domicilio:: " + coloniaId);

		} catch (Exception e) {
			log.error("Error al obtener concepto Domicilio:: " + e.getMessage());
			e.printStackTrace();
		}
		return coloniaId;
	}

	public List<Map<String, Object>> buscarInfoDomicilioV2(int claveColonia) {
		List<Map<String, Object>> rows = null;
		log.info("Entra a obtener buscarInfoDomicilioV2: " + claveColonia);
		try {
			rows = jdbcTemplate.queryForList(infoDomicilioV2, claveColonia);
		} catch (Exception e) {
			log.error("error al buscar la informacion del domicilio: " + e.getMessage());
		}
		return rows;

	}

	public int insertarConceptoColonia(String numero, String valor) {
		log.info("Entra a nuevoConceptoSolicitante");
		String numSolicitante = numero;
		int registros = 0;
		try {
			log.debug("#numero de solicitante :: " + numSolicitante);
			log.debug(insertarConceptoSolicitante);
			registros = jdbcTemplatePr.update(insertarConceptoSolicitante, numSolicitante, valor, "");
			log.info("Concepto Colonia registrado");

			log.info("Registros Insertados = " + registros);
		} catch (Exception e) {
			log.error("nuevoConceptoSolicitante", e);
			return registros;
		}

		return registros;
	}

	/**
	 *
	 * @param valor
	 * @param numero
	 * @param usuarioId
	 * @return
	 */
	public int actualizarConceptoColoniaSolicitante(String valor, String numero, int usuarioId) {
		log.info("Entra a nuevoConceptoSolicitante");
		String numSolicitante = numero;
		int registros = 0;
		try {
			log.debug("#numero de solicitante :: " + numSolicitante);
			log.debug(actualizarConceptoColoniaSolicitante);
			registros = jdbcTemplatePr.update(actualizarConceptoColoniaSolicitante, valor, usuarioId, numero);
			log.info("Concepto Colonia registrado");

			log.info("Registros Insertados = " + registros);
		} catch (Exception e) {
			log.error("nuevoConceptoSolicitante", e);
			return registros;
		}

		return registros;
	}

	/**
	 *
	 * @param domicilio
	 * @param req
	 * @return
	 */
	public int actualizarDomicilioSolicitante(String domicilio,  ActualizarSolicitanteDomicilioReq req) {
		log.info("Entra a nuevoConceptoSolicitante");
		int registros = 0;
		try {
			log.debug("#Solicitante Data:: " + req.toString());
			log.debug(actualizarDomicilioSolicitante);
			registros = jdbcTemplatePr.update(actualizarDomicilioSolicitante,
																	domicilio,
																	req.getUsuarioId(),
																	req.getColoniaId(),
																	req.getCatDomicilio1(),
																	req.getCatDomicilio2(),
																	req.getCatDomicilio3(),
																	req.getCatDomicilio4(),
																	req.getCatDomicilio5(),
																	req.getDescripcionDomicilio1(),
																	req.getDescripcionDomicilio2(),
																	req.getDescripcionDomicilio3(),
																	req.getDescripcionDomicilio4(),
																	req.getDescripcionDomicilio5(),
																	req.getObservaciones(),
																	req.getSolicitanteId()
																							);
			log.info("Concepto Colonia registrado exitosamente");

			log.info("Registros Insertados = " + registros);
		} catch (Exception e) {
			log.error("nuevoConceptoSolicitante", e);
			return registros;
		}
		return registros;
	}

	public ActualizarSolicitanteDomicilioReq obtenerDomicilioSolicitante(String personaId) {
		ActualizarSolicitanteDomicilioReq domicilio = new ActualizarSolicitanteDomicilioReq();
		log.info("Entra a obtener domicilio: personaId" + personaId);

		try {
			Map<String, Object> row = jdbcTemplatePr.queryForMap(obtenerDomicilioSolicitante, personaId);

			if (!row.isEmpty()) {
				domicilio.setDomicilio((String) row.get("domicilio"));
				domicilio.setUsuarioId((Integer) row.get("modificado_por"));
				domicilio.setColoniaId((Integer) row.get("colonia"));
				domicilio.setCatDomicilio1((Integer) row.get("cat_domicilio1"));
				domicilio.setCatDomicilio2((Integer) row.get("cat_domicilio2"));
				domicilio.setCatDomicilio3((Integer) row.get("cat_domicilio3"));
				domicilio.setCatDomicilio4((Integer) row.get("cat_domicilio4"));
				domicilio.setCatDomicilio5((Integer) row.get("cat_domicilio5"));
				domicilio.setDescripcionDomicilio1((String) row.get("descripcion_domicilio1"));
				domicilio.setDescripcionDomicilio2((String) row.get("descripcion_domicilio2"));
				domicilio.setDescripcionDomicilio3((String) row.get("descripcion_domicilio3"));
				domicilio.setDescripcionDomicilio4((String) row.get("descripcion_domicilio4"));
				domicilio.setDescripcionDomicilio5((String) row.get("descripcion_domicilio5"));
				domicilio.setObservaciones((String) row.get("domicilo_observaciones"));
			}

		} catch (Exception e) {
			log.error("Error al obtener concepto Domicilio:: " + e.getMessage(), e);
		}

		return domicilio;
	}



	public String getObtenerDomicilioSolicitante() {
		return obtenerDomicilioSolicitante;
	}

	public void setObtenerDomicilioSolicitante(String obtenerDomicilioSolicitante) {
		this.obtenerDomicilioSolicitante = obtenerDomicilioSolicitante;
	}

	public String getActualizarDomicilioSolicitante() {
		return actualizarDomicilioSolicitante;
	}

	public void setActualizarDomicilioSolicitante(String actualizarDomicilioSolicitante) {
		this.actualizarDomicilioSolicitante = actualizarDomicilioSolicitante;
	}

	public String getActualizarConceptoColoniaSolicitante() {
		return actualizarConceptoColoniaSolicitante;
	}

	public void setActualizarConceptoColoniaSolicitante(String actualizarConceptoColoniaSolicitante) {
		this.actualizarConceptoColoniaSolicitante = actualizarConceptoColoniaSolicitante;
	}

	public String getInsertarConceptoSolicitante() {
		return insertarConceptoSolicitante;
	}

	public void setInsertarConceptoSolicitante(String insertarConceptoSolicitante) {
		this.insertarConceptoSolicitante = insertarConceptoSolicitante;
	}

	public String getInfoDomicilioV2() {
		return infoDomicilioV2;
	}

	public void setInfoDomicilioV2(String infoDomicilioV2) {
		this.infoDomicilioV2 = infoDomicilioV2;
	}

	/**
	 * @return the jdbcTemplatePr
	 */
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	/**
	 * @param jdbcTemplatePr the jdbcTemplatePr to set
	 */
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @return the buscarSolicitanteByNumero
	 */
	public String getBuscarSolicitanteByNumero() {
		return buscarSolicitanteByNumero;
	}

	/**
	 * @param buscarSolicitanteByNumero the buscarSolicitanteByNumero to set
	 */
	public void setBuscarSolicitanteByNumero(String buscarSolicitanteByNumero) {
		this.buscarSolicitanteByNumero = buscarSolicitanteByNumero;
	}

	/**
	 * @return the buscarSolicitenteExistenteByRfcCurp
	 */
	public String getBuscarSolicitenteExistenteByRfcCurp() {
		return buscarSolicitenteExistenteByRfcCurp;
	}

	/**
	 * @param buscarSolicitenteExistenteByRfcCurp the buscarSolicitenteExistenteByRfcCurp to set
	 */
	public void setBuscarSolicitenteExistenteByRfcCurp(String buscarSolicitenteExistenteByRfcCurp) {
		this.buscarSolicitenteExistenteByRfcCurp = buscarSolicitenteExistenteByRfcCurp;
	}

	/**
	 * @return the sigSecSolicitante
	 */
	public String getSigSecSolicitante() {
		return sigSecSolicitante;
	}

	/**
	 * @param sigSecSolicitante the sigSecSolicitante to set
	 */
	public void setSigSecSolicitante(String sigSecSolicitante) {
		this.sigSecSolicitante = sigSecSolicitante;
	}

	/**
	 * @return the nuevoSolicitante
	 */
	public String getNuevoSolicitante() {
		return nuevoSolicitante;
	}

	/**
	 * @param nuevoSolicitante the nuevoSolicitante to set
	 */
	public void setNuevoSolicitante(String nuevoSolicitante) {
		this.nuevoSolicitante = nuevoSolicitante;
	}

	/**
	 * @return the actualizaSolicitante
	 */
	public String getActualizaSolicitante() {
		return actualizaSolicitante;
	}

	/**
	 * @param actualizaSolicitante the actualizaSolicitante to set
	 */
	public void setActualizaSolicitante(String actualizaSolicitante) {
		this.actualizaSolicitante = actualizaSolicitante;
	}

	/**
	 * @return the obtenerEdoNacByCveCURP
	 */
	public String getObtenerEdoNacByCveCURP() {
		return obtenerEdoNacByCveCURP;
	}

	/**
	 * @param obtenerEdoNacByCveCURP the obtenerEdoNacByCveCURP to set
	 */
	public void setObtenerEdoNacByCveCURP(String obtenerEdoNacByCveCURP) {
		this.obtenerEdoNacByCveCURP = obtenerEdoNacByCveCURP;
	}

	public String getRegistrarSolicitanteCuentaSimplificada() {
		return registrarSolicitanteCuentaSimplificada;
	}

	public void setRegistrarSolicitanteCuentaSimplificada(String registrarSolicitanteCuentaSimplificada) {
		this.registrarSolicitanteCuentaSimplificada = registrarSolicitanteCuentaSimplificada;
	}

	public String getObtenerNombreSolicitanteByNumero() {
		return obtenerNombreSolicitanteByNumero;
	}

	public void setObtenerNombreSolicitanteByNumero(String obtenerNombreSolicitanteByNumero) {
		this.obtenerNombreSolicitanteByNumero = obtenerNombreSolicitanteByNumero;
	}

	public String getNuevoSolicitanteV2() {
		return nuevoSolicitanteV2;
	}

	public void setNuevoSolicitanteV2(String nuevoSolicitanteV2) {
		this.nuevoSolicitanteV2 = nuevoSolicitanteV2;
	}

	public String getActualizaSolicitanteV2() {
		return actualizaSolicitanteV2;
	}

	public void setActualizaSolicitanteV2(String actualizaSolicitanteV2) {
		this.actualizaSolicitanteV2 = actualizaSolicitanteV2;
	}

	public String getAltaCanalAtencion() {
		return altaCanalAtencion;
	}

	public void setAltaCanalAtencion(String altaCanalAtencion) {
		this.altaCanalAtencion = altaCanalAtencion;
	}

	public String getValidarCodigoAutorizacionBiometrico() {
		return validarCodigoAutorizacionBiometrico;
	}

	public void setValidarCodigoAutorizacionBiometrico(String validarCodigoAutorizacionBiometrico) {
		this.validarCodigoAutorizacionBiometrico = validarCodigoAutorizacionBiometrico;
	}

	public String getAltaPais() {
		return altaPais;
	}

	public void setAltaPais(String altaPais) {
		this.altaPais = altaPais;
	}

	public String getBuscarInfoSolicitanteF() {
		return buscarInfoSolicitanteF;
	}

	public void setBuscarInfoSolicitanteF(String buscarInfoSolicitanteF) {
		this.buscarInfoSolicitanteF = buscarInfoSolicitanteF;
	}

	public String getBuscarInfoSolicitanteM() {
		return buscarInfoSolicitanteM;
	}

	public void setBuscarInfoSolicitanteM(String buscarInfoSolicitanteM) {
		this.buscarInfoSolicitanteM = buscarInfoSolicitanteM;
	}

	public String getObtenerNombrePais() {
		return obtenerNombrePais;
	}

	public void setObtenerNombrePais(String obtenerNombrePais) {
		this.obtenerNombrePais = obtenerNombrePais;
	}

	public String getObtenerCorreo() {
		return obtenerCorreo;
	}

	public void setObtenerCorreo(String obtenerCorreo) {
		this.obtenerCorreo = obtenerCorreo;
	}

	public String getLongitudSubirNivel() {
		return longitudSubirNivel;
	}

	public void setLongitudSubirNivel(String longitudSubirNivel) {
		this.longitudSubirNivel = longitudSubirNivel;
	}

	public String getLatitudSubirNivel() {
		return latitudSubirNivel;
	}

	public void setLatitudSubirNivel(String latitudSubirNivel) {
		this.latitudSubirNivel = latitudSubirNivel;
	}

	public String getActualizaSolicitanteSubirNivelCuenta() {
		return actualizaSolicitanteSubirNivelCuenta;
	}

	public void setActualizaSolicitanteSubirNivelCuenta(String actualizaSolicitanteSubirNivelCuenta) {
		this.actualizaSolicitanteSubirNivelCuenta = actualizaSolicitanteSubirNivelCuenta;
	}

	public String getAltaConcepto() {
		return altaConcepto;
	}

	public void setAltaConcepto(String altaConcepto) {
		this.altaConcepto = altaConcepto;
	}

	public String getCopiaDatosCeroProcrea() {
		return copiaDatosCeroProcrea;
	}

	public void setCopiaDatosCeroProcrea(String copiaDatosCeroProcrea) {
		this.copiaDatosCeroProcrea = copiaDatosCeroProcrea;
	}

	public String getSetDatosCeroProcrea() {
		return setDatosCeroProcrea;
	}

	public void setSetDatosCeroProcrea(String setDatosCeroProcrea) {
		this.setDatosCeroProcrea = setDatosCeroProcrea;
	}

	public String getBorraConceptoXIdSolIdCat() {
		return borraConceptoXIdSolIdCat;
	}

	public void setBorraConceptoXIdSolIdCat(String borraConceptoXIdSolIdCat) {
		this.borraConceptoXIdSolIdCat = borraConceptoXIdSolIdCat;
	}

	public String getObtenerDomicilio() {
		return obtenerDomicilio;
	}

	public void setObtenerDomicilio(String obtenerDomicilio) {
		this.obtenerDomicilio = obtenerDomicilio;
	}

	public String getConsultaPersonaId() {
		return consultaPersonaId;
	}

	public void setConsultaPersonaId(String consultaPersonaId) {
		this.consultaPersonaId = consultaPersonaId;
	}

	public String getConsultaPersonaIdPorCuenta() {
		return consultaPersonaIdPorCuenta;
	}

	public void setConsultaPersonaIdPorCuenta(String consultaPersonaIdPorCuenta) {
		this.consultaPersonaIdPorCuenta = consultaPersonaIdPorCuenta;
	}

	public String getConsultaSolicitante() {
		return consultaSolicitante;
	}

	public void setConsultaSolicitante(String consultaSolicitante) {
		this.consultaSolicitante = consultaSolicitante;
	}

	public String getBuscarCuentaPorPersona_id() {
		return buscarCuentaPorPersona_id;
	}

	public void setBuscarCuentaPorPersona_id(String buscarCuentaPorPersona_id) {
		this.buscarCuentaPorPersona_id = buscarCuentaPorPersona_id;
	}

	public String getConsultaEntrega() {
		return consultaEntrega;
	}

	public void setConsultaEntrega(String consultaEntrega) {
		this.consultaEntrega = consultaEntrega;
	}

	public String getValidarCodigoAutorizacion() {
		return validarCodigoAutorizacion;
	}

	public void setValidarCodigoAutorizacion(String validarCodigoAutorizacion) {
		this.validarCodigoAutorizacion = validarCodigoAutorizacion;
	}

	public String getCambiarCodigoAutorizacionBiometrico() {
		return cambiarCodigoAutorizacionBiometrico;
	}

	public void setCambiarCodigoAutorizacionBiometrico(String cambiarCodigoAutorizacionBiometrico) {
		this.cambiarCodigoAutorizacionBiometrico = cambiarCodigoAutorizacionBiometrico;
	}

	public String getInsertarBitacoraCodigoAutorizacion() {
		return insertarBitacoraCodigoAutorizacion;
	}

	public void setInsertarBitacoraCodigoAutorizacion(String insertarBitacoraCodigoAutorizacion) {
		this.insertarBitacoraCodigoAutorizacion = insertarBitacoraCodigoAutorizacion;
	}

	public String getBuscarSiExisteServicio() {
		return buscarSiExisteServicio;
	}

	public void setBuscarSiExisteServicio(String buscarSiExisteServicio) {
		this.buscarSiExisteServicio = buscarSiExisteServicio;
	}

	public String getBuscarIdColoniaPr() {
		return buscarIdColoniaPr;
	}

	public void setBuscarIdColoniaPr(String buscarIdColoniaPr) {
		this.buscarIdColoniaPr = buscarIdColoniaPr;
	}

	public String getCambiarCodigoAutorizacion() {
		return cambiarCodigoAutorizacion;
	}

	public void setCambiarCodigoAutorizacion(String cambiarCodigoAutorizacion) {
		this.cambiarCodigoAutorizacion = cambiarCodigoAutorizacion;
	}

	public String getInfoDomicilioPr() {
		return infoDomicilioPr;
	}

	public void setInfoDomicilioPr(String infoDomicilioPr) {
		this.infoDomicilioPr = infoDomicilioPr;
	}

	public String getInsertarConceptoSolicitanteBeneficiario() {
		return insertarConceptoSolicitanteBeneficiario;
	}

	public void setInsertarConceptoSolicitanteBeneficiario(String insertarConceptoSolicitanteBeneficiario) {
		this.insertarConceptoSolicitanteBeneficiario = insertarConceptoSolicitanteBeneficiario;
	}

	public String getReferenciaCuentaAmbiente() {
		return referenciaCuentaAmbiente;
	}

	public void setReferenciaCuentaAmbiente(String referenciaCuentaAmbiente) {
		this.referenciaCuentaAmbiente = referenciaCuentaAmbiente;
	}

	public String getConsultaMediosAcceso() {
		return consultaMediosAcceso;
	}

	public void setConsultaMediosAcceso(String consultaMediosAcceso) {
		this.consultaMediosAcceso = consultaMediosAcceso;
	}

	public String getNivelCuentaAmbiente() {
		return nivelCuentaAmbiente;
	}

	public void setNivelCuentaAmbiente(String nivelCuentaAmbiente) {
		this.nivelCuentaAmbiente = nivelCuentaAmbiente;
	}

	public String getConsultaDetalleCuenta() {
		return consultaDetalleCuenta;
	}

	public void setConsultaDetalleCuenta(String consultaDetalleCuenta) {
		this.consultaDetalleCuenta = consultaDetalleCuenta;
	}

	public String getConsultaAspCuentas() {
		return consultaAspCuentas;
	}

	public void setConsultaAspCuentas(String consultaAspCuentas) {
		this.consultaAspCuentas = consultaAspCuentas;
	}

	public String getConsultaEstadoNac() {
		return consultaEstadoNac;
	}

	public void setConsultaEstadoNac(String consultaEstadoNac) {
		this.consultaEstadoNac = consultaEstadoNac;
	}

	public String getConsultaOcupaciones() {
		return consultaOcupaciones;
	}

	public void setConsultaOcupaciones(String consultaOcupaciones) {
		this.consultaOcupaciones = consultaOcupaciones;
	}

	public String getBuscarClaveEstadoPorCurp() {
		return buscarClaveEstadoPorCurp;
	}

	public void setBuscarClaveEstadoPorCurp(String buscarClaveEstadoPorCurp) {
		this.buscarClaveEstadoPorCurp = buscarClaveEstadoPorCurp;
	}

	public String getObtenerPais() {
		return obtenerPais;
	}

	public void setObtenerPais(String obtenerPais) {
		this.obtenerPais = obtenerPais;
	}

	public String getGuardarIdConsulta() {
		return guardarIdConsulta;
	}

	public void setGuardarIdConsulta(String guardarIdConsulta) {
		this.guardarIdConsulta = guardarIdConsulta;
	}

	public String getConcatenarDomicilio() {
		return concatenarDomicilio;
	}

	public void setConcatenarDomicilio(String concatenarDomicilio) {
		this.concatenarDomicilio = concatenarDomicilio;
	}

	public String getAltaPersonaF() {
		return altaPersonaF;
	}

	public void setAltaPersonaF(String altaPersonaF) {
		this.altaPersonaF = altaPersonaF;
	}

	public String getAltaPersonaM() {
		return altaPersonaM;
	}

	public void setAltaPersonaM(String altaPersonaM) {
		this.altaPersonaM = altaPersonaM;
	}

	public String getBuscarIdColonia() {
		return buscarIdColonia;
	}

	public void setBuscarIdColonia(String buscarIdColonia) {
		this.buscarIdColonia = buscarIdColonia;
	}

	public String getAltaConIne() {
		return altaConIne;
	}

	public void setAltaConIne(String altaConIne) {
		this.altaConIne = altaConIne;
	}

	public String getInsertIdentificacion() {
		return insertIdentificacion;
	}

	public void setInsertIdentificacion(String insertIdentificacion) {
		this.insertIdentificacion = insertIdentificacion;
	}

	public String getBuscarEstado() {
		return buscarEstado;
	}

	public void setBuscarEstado(String buscarEstado) {
		this.buscarEstado = buscarEstado;
	}

	public String getBuscarSiExiste() {
		return buscarSiExiste;
	}

	public void setBuscarSiExiste(String buscarSiExiste) {
		this.buscarSiExiste = buscarSiExiste;
	}

	public String getBuscarSolicitanteByCurp() {
		return buscarSolicitanteByCurp;
	}

	public void setBuscarSolicitanteByCurp(String buscarSolicitanteByCurp) {
		this.buscarSolicitanteByCurp = buscarSolicitanteByCurp;
	}

	public String getBuscarSolicitanteByRfc() {
		return buscarSolicitanteByRfc;
	}

	public void setBuscarSolicitanteByRfc(String buscarSolicitanteByRfc) {
		this.buscarSolicitanteByRfc = buscarSolicitanteByRfc;
	}

	public String getBuscarSiExisteMoral() {
		return buscarSiExisteMoral;
	}

	public void setBuscarSiExisteMoral(String buscarSiExisteMoral) {
		this.buscarSiExisteMoral = buscarSiExisteMoral;
	}

	public String getInfoDomicilio() {
		return infoDomicilio;
	}

	public void setInfoDomicilio(String infoDomicilio) {
		this.infoDomicilio = infoDomicilio;
	}

	public String getAltaSerieFirma() {
		return altaSerieFirma;
	}

	public void setAltaSerieFirma(String altaSerieFirma) {
		this.altaSerieFirma = altaSerieFirma;
	}

	public String getBuscarIdEstado() {
		return buscarIdEstado;
	}

	public void setBuscarIdEstado(String buscarIdEstado) {
		this.buscarIdEstado = buscarIdEstado;
	}

	public String getGetDocumentacionObligatoria() {
		return getDocumentacionObligatoria;
	}

	public void setGetDocumentacionObligatoria(String getDocumentacionObligatoria) {
		this.getDocumentacionObligatoria = getDocumentacionObligatoria;
	}

	public String getGetDocumentacionCargada() {
		return getDocumentacionCargada;
	}

	public void setGetDocumentacionCargada(String getDocumentacionCargada) {
		this.getDocumentacionCargada = getDocumentacionCargada;
	}

	public String getObtenerRutaIne() {
		return obtenerRutaIne;
	}

	public void setObtenerRutaIne(String obtenerRutaIne) {
		this.obtenerRutaIne = obtenerRutaIne;
	}

	public String getConsultaBeneficiario() {
		return consultaBeneficiario;
	}

	public void setConsultaBeneficiario(String consultaBeneficiario) {
		this.consultaBeneficiario = consultaBeneficiario;
	}

	public String getConsultaListaParentesco() {
		return consultaListaParentesco;
	}

	public void setConsultaListaParentesco(String consultaListaParentesco) {
		this.consultaListaParentesco = consultaListaParentesco;
	}

	public String getConsultaSaldoDisponibleMiAhorro() {
		return consultaSaldoDisponibleMiAhorro;
	}

	public void setConsultaSaldoDisponibleMiAhorro(String consultaSaldoDisponibleMiAhorro) {
		this.consultaSaldoDisponibleMiAhorro = consultaSaldoDisponibleMiAhorro;
	}

	public String getConsultaCuentaPorProducto() {
		return consultaCuentaPorProducto;
	}

	public void setConsultaCuentaPorProducto(String consultaCuentaPorProducto) {
		this.consultaCuentaPorProducto = consultaCuentaPorProducto;
	}

	public String getConsultaAspCuentasProcrea() {
		return consultaAspCuentasProcrea;
	}

	public void setConsultaAspCuentasProcrea(String consultaAspCuentasProcrea) {
		this.consultaAspCuentasProcrea = consultaAspCuentasProcrea;
	}

	public String getConsultaCuentaPorProductoYSolicitante() {
		return consultaCuentaPorProductoYSolicitante;
	}

	public void setConsultaCuentaPorProductoYSolicitante(String consultaCuentaPorProductoYSolicitante) {
		this.consultaCuentaPorProductoYSolicitante = consultaCuentaPorProductoYSolicitante;
	}

	public String getConsultaCuentaPorProductoYSolicitanteProcrea() {
		return consultaCuentaPorProductoYSolicitanteProcrea;
	}

	public void setConsultaCuentaPorProductoYSolicitanteProcrea(String consultaCuentaPorProductoYSolicitanteProcrea) {
		this.consultaCuentaPorProductoYSolicitanteProcrea = consultaCuentaPorProductoYSolicitanteProcrea;
	}

	public String getConsultaCuentaPorProductoProcrea() {
		return consultaCuentaPorProductoProcrea;
	}

	public void setConsultaCuentaPorProductoProcrea(String consultaCuentaPorProductoProcrea) {
		this.consultaCuentaPorProductoProcrea = consultaCuentaPorProductoProcrea;
	}

	public String getObtenerSolicitantePorCorreo() {
		return obtenerSolicitantePorCorreo;
	}

	public void setObtenerSolicitantePorCorreo(String obtenerSolicitantePorCorreo) {
		this.obtenerSolicitantePorCorreo = obtenerSolicitantePorCorreo;
	}

	public String getObtenerCurpRfcPorSolicitanteId() {
		return obtenerCurpRfcPorSolicitanteId;
	}

	public void setObtenerCurpRfcPorSolicitanteId(String obtenerCurpRfcPorSolicitanteId) {
		this.obtenerCurpRfcPorSolicitanteId = obtenerCurpRfcPorSolicitanteId;
	}

	public String getActualizarCurp() {
        return actualizarCurp;
    }

    public void setActualizarCurp(String actualizarCurp) {
        this.actualizarCurp = actualizarCurp;
    }

    public String getActualizarRfc() {
        return actualizarRfc;
    }

    public void setActualizarRfc(String actualizarRfc) {
        this.actualizarRfc = actualizarRfc;
    }

    public String getObtenerCurpRfc() {
        return obtenerCurpRfc;
    }

    public void setObtenerCurpRfc(String obtenerCurpRfc) {
        this.obtenerCurpRfc = obtenerCurpRfc;
    }

	public String getActualizarCorreo() {
		return actualizarCorreo;
	}

	public void setActualizarCorreo(String actualizarCorreo) {
		this.actualizarCorreo = actualizarCorreo;
	}

	public String getObtenerConceptoDomicilio() {
		return obtenerConceptoDomicilio;
	}

	public void setObtenerConceptoDomicilio(String obtenerConceptoDomicilio) {
		this.obtenerConceptoDomicilio = obtenerConceptoDomicilio;
	}
}
