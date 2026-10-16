package net.cero.ahorro.logica;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mx.Req.ImagenAlfrescoReq;
import com.mx.Res.RespuestaCommons;

import net.cero.data.AgenteOBJ;
import net.cero.data.AhorroAlfrescoOBJ;
import net.cero.data.AhorroContrato;
import net.cero.data.AhorroContratoDatos;
import net.cero.data.AhorroCuentaOBJ;
import net.cero.data.AhorroSaldos;
import net.cero.data.AhorroTransferenciaReq;
import net.cero.data.AhorroTransferenciaReqOBJ;
import net.cero.data.AutorizacionesPendientesReq;
import net.cero.data.CampaniaOBJ;
import net.cero.data.ColoniaOBJ;
import net.cero.data.DirectorioTelefonicoOBJ;
import net.cero.data.GeneraContratoServElecReq;
import net.cero.data.GeneraDisposicionesLegales;
import net.cero.data.GeneraNotificacionCallCenterReqOBJ;
import net.cero.data.GeneraRegistroContratoReq;
import net.cero.data.GeneraReporteContratoReq;
import net.cero.data.GeneraReporteTarjetaReq;
import net.cero.data.GuardarServiciosDigitalesReq;
import net.cero.data.IneOcrRespOBJ;
import net.cero.data.RegionesOBJ;
import net.cero.data.RegistroCodiOBJ;
import net.cero.data.RegistroCuentaAhorroSimplificadaReq;
import net.cero.data.RegistroServiciosDigitalesOBJ;
import net.cero.data.ResponseService;
import net.cero.data.Respuesta;
import net.cero.data.SMS;
import net.cero.data.Solicitante;
import net.cero.data.ValidacionOcrReq;
import net.cero.promesi.AuthHeadersRequest;
import net.cero.promesi.RestCall2;
import net.cero.ahorro.logica.GeneracionFolioLogic;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.Encrypted;
import net.cero.seguridad.utilidades.HeaderWS;
import net.cero.seguridad.utilidades.iso9564;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AgenteDAO;
import net.cero.spring.dao.AhorroConceptosDAO;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroContratoDatosDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.AuditoriaDAO;
import net.cero.spring.dao.CampaniaDAO;
import net.cero.spring.dao.CanalesDAO;
import net.cero.spring.dao.ColoniaDAO;
import net.cero.spring.dao.DirectorioTelefonicoDAO;
import net.cero.spring.dao.PINDAO;
import net.cero.spring.dao.ParametrosGeneralesDAO;
import net.cero.spring.dao.RegionesDAO;
import net.cero.spring.dao.RegistroCodiDAO;
import net.cero.spring.dao.RegistroServiciosDigitalesDAO;
import net.cero.spring.dao.SolicitanteDAO;
import net.cero.spring.dao.ValorUdiDAO;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Logica de negocio para registrar una cuenta de ahorro simplificada.
 * 
 * @author Israel
 * @version 1.0 01/06/19
 */
@Log4j2
public class RegistroCuentaAhorroSimplificadaLogic {
	private static Apps apps = null;

	private static SolicitanteDAO sdao;
	private static AhorroContratoDAO adao;
	private static AhorroContratoDatosDAO acddao;
	private static ColoniaDAO coldao;
	private static RegionesDAO regdao;
	private static AgenteDAO agdao;
	private static DirectorioTelefonicoDAO dtdao;
	private static AhorroSaldosDAO asdao;
	private static ParametrosGeneralesDAO pgdao;

	private static RegistroCodiDAO rcdao;
	private static RegistroServiciosDigitalesDAO rsdao;
	private static RegistroServiciosDigitalesDAO rdao;
	private static PINDAO pdao;
	private static AuditoriaDAO audao;
	private static CanalesDAO cdao;

	private static CampaniaDAO cpdao;
	private static AhorroConceptosDAO acdao;
	
	private static ValorUdiDAO vdao;

	private static AhorroTransferenciaLogic ahorroTransferenciaLogic;

	private static Gson gson;

	private static Integer productoId;
	private static Integer rendimientoId;
	private static Integer usuarioId;
	private static Integer comoEnteroId;
	private static Integer acesorId;
	private static Integer tipoCapitalizarId;
	private static Integer idCatTelefono;
	private static String observacionDirTel;

	private static String REMITENTE_NOTIFICACIONES = "procesos@aspintegraopciones.com";
	private static String ASUNTO_CORREO_ANEXO = "Documentación de cuenta de ahorro";
	private static String ASUNTO_CORREO_CONTRATO = "Documentación de cuenta de ahorro";
	private static String ASUNTO_CORREO_NIP = "Datos para utilizar su cuenta de ahorro ASP";

	private static AuthHeadersRequest headerAuth = null;

	private SaldoAhorro consultarSaldo;
	
	private AhorroAlfrescoOBJ obj;

	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			sdao = (SolicitanteDAO) s.getApplicationContext().getBean("SolicitanteDAO");
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			acddao = (AhorroContratoDatosDAO) s.getApplicationContext().getBean("AhorroContratoDatosDAO");
			asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			coldao = (ColoniaDAO) s.getApplicationContext().getBean("ColoniaDAO");
			regdao = (RegionesDAO) s.getApplicationContext().getBean("RegionesDAO");
			agdao = (AgenteDAO) s.getApplicationContext().getBean("AgenteDAO");
			dtdao = (DirectorioTelefonicoDAO) s.getApplicationContext().getBean("DirectorioTelefonicoDAO");

			rcdao = (RegistroCodiDAO) s.getApplicationContext().getBean("RegistroCodiDAO");
			rsdao = (RegistroServiciosDigitalesDAO) s.getApplicationContext().getBean("RegistroServiciosDigitalesDAO");
			// psdao = (PosicionGlobalDAO)
			// s.getApplicationContext().getBean("PosicionGlobalDAO");
			rdao = (RegistroServiciosDigitalesDAO) s.getApplicationContext().getBean("RegistroServiciosDigitalesDAO");
			pdao = (PINDAO) s.getApplicationContext().getBean("PINDAO");
			audao = (AuditoriaDAO) s.getApplicationContext().getBean("AuditoriaDAO");
			cdao = (CanalesDAO) s.getApplicationContext().getBean("CanalesDAO");
			pgdao = (ParametrosGeneralesDAO) s.getApplicationContext().getBean("ParametrosGeneralesDAO");

			cpdao = (CampaniaDAO) s.getApplicationContext().getBean("CampaniaDAO");
			acdao = (AhorroConceptosDAO) s.getApplicationContext().getBean("AhorroConceptosDAO");
			
			vdao = (ValorUdiDAO) s.getApplicationContext().getBean("ValorUdiDAO");

			ahorroTransferenciaLogic = new AhorroTransferenciaLogic();
			gson = new Gson();

			setProductoId(6);
			setRendimientoId(35);
			setUsuarioId(9);
			setComoEnteroId(19);
			setAcesorId(9398);
			setTipoCapitalizarId(35);
			setIdCatTelefono(7);
			setObservacionDirTel("Telefono para uso de CoDi");

			headerAuth = new AuthHeadersRequest("SISTEMAS");

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public ResponseService registroCuentaAhorroSimplificada(RegistroCuentaAhorroSimplificadaReq req) {
		initialized();
		consultarSaldo = new SaldoAhorro();
		ResponseService respuesta = new ResponseService();
		Boolean solicitanteRegistrado = false;
		AhorroContrato ahorroContratoNuevo = null;
		AhorroSaldos ahorroSaldosNuevo = null;
		String cuentaAhorroNueva = "";
		String cuentaCorreo = req.getEmail();
		String nombreSolicitante = "";
		CampaniaOBJ cam = new CampaniaOBJ();
		AhorroTransferenciaLogic trans = new AhorroTransferenciaLogic();

		boolean fondeoCodigo = false;

		String numeroSolicitanteNuevo = "";
		try {
			// String resultadoOCRStr = verificaOSCDummy(req.getIne());
			// String resultadoOCRStr = verificaOSCDummyMerino(req.getIne());
			// String resultadoOCRStr = verificaOSC(req.getIne());
			// log.info("## Resultado de la validacion OCR :: " +
			// resultadoOCRStr);
			String validaInfo = "";
			Boolean validaInformacion = true;
			;
			validaInfo = pgdao.validacionCuentaSimplificada();
			log.info("Valida informacion :: " + validaInfo);
			if (validaInfo == null) {
				validaInformacion = false;
			} else {
				if (validaInfo.isEmpty()) {
					validaInformacion = false;
				} else {
					if (validaInfo.equals("NO")) {
						validaInformacion = false;
					} else {
						validaInformacion = true;
					}
				}
			}

			Boolean fondeaCuenta = false;
			fondeaCuenta = pgdao.fondeaCuentaSimplificada();

			if (req.getCodigoPromocion() != null && !"".equals(req.getCodigoPromocion())) {
				cam = cpdao.buscarCampaniaPorCodigo(req.getCodigoPromocion());

				if (cam == null) {
					respuesta.setCode(-12);
					respuesta.setMenssage("Código de promoción inválido");
					return respuesta;
				} else {
					Respuesta saldoAhorro = consultarSaldo.consultaSaldoAhorro(cam.getCuentaAhorro());
					log.info(
							saldoAhorro.getCodigo() + ": " + saldoAhorro.getMensaje() + " - " + saldoAhorro.getData());
					Double saldo = Double.parseDouble(saldoAhorro.getData());
					if (saldo >= cam.getIncentivo())
						fondeoCodigo = true;
					else
						fondeoCodigo = false;
				}
			}
			
			
			OkHttpClient client = new OkHttpClient();
            String auth = Credentials.basic("ASP", "a5p2017$");
            MediaType media = MediaType.parse("application/json; charset=utf-8");
            Request request;
            Response resp;
            String host = ConstantesUtil.WS_CERO_AHORRO+"/CuentaAhorroEstadoSwitch";

            request = new Request.Builder().url(host).get()
                    .header("Authorization", auth).build();
            resp = client.newCall(request).execute();
            String porm = resp.body().string();
            log.info("###PORM " + porm);
            Respuesta resp2 = gson.fromJson(porm, Respuesta.class);
            
			IneOcrRespOBJ ineOcer = new IneOcrRespOBJ();
			ineOcer = gson.fromJson(req.getIneOcr(), IneOcrRespOBJ.class);

			if (ineOcer != null) {
				if (ineOcer.getEstatus() != null && resp2.getData() == "1")  {
					log.info("Estatus ocr :: " + ineOcer.getEstatus());
					if ("ERROR".equals(ineOcer.getEstatus())) {
						respuesta.setCode(-11);
						respuesta.setMenssage("No se pudo realizar el registro, " + ineOcer.getMensaje());
						return respuesta;
					}
				}

				// Evitar que nombre y apellido del solicitante tenga null:
				String nombres = ineOcer.getNombres() == null ? "" : ineOcer.getNombres();
				String pApellido = ineOcer.getPrimerApellido() == null ? "" : ineOcer.getPrimerApellido();
				String mApellido = ineOcer.getSegundoApellido() == null ? "" : ineOcer.getSegundoApellido();
				ineOcer.setNombres(nombres);
				ineOcer.setPrimerApellido(pApellido);
				ineOcer.setSegundoApellido(mApellido);

				Solicitante solicitanteExistente = new Solicitante();

				if (!validaInformacion) {
					solicitanteRegistrado = false;
				} else {
					log.info("## RFC :: " + req.getRfc());
					log.info("## CURP :: " + ineOcer.getCurp());
					solicitanteExistente = sdao.buscarSolicitenteExistenteByRfcCurp(req.getRfc(), ineOcer.getCurp());
					if (solicitanteExistente != null) {
						if (solicitanteExistente.getNumero() != null) {
							if (!solicitanteExistente.getNumero().isEmpty()) {
								solicitanteRegistrado = true;
							}
						}
					}
				}

				if (!solicitanteRegistrado) {
					log.info("## Soliciante nuevo");
					// SECCION PARA REGISTRAR SOLICITANTE
					Solicitante solicitanteNuevo = new Solicitante();
					solicitanteNuevo.setNombre(ineOcer.getNombres() + " " + ineOcer.getPrimerApellido() + " "
							+ ineOcer.getSegundoApellido());
					solicitanteNuevo.setNombreP(ineOcer.getNombres());
					solicitanteNuevo.setApellidos(ineOcer.getPrimerApellido());
					solicitanteNuevo.setApellidoM(ineOcer.getSegundoApellido());
					nombreSolicitante = solicitanteNuevo.getNombre();

					String domicilioCompleto = construirDomicilio(ineOcer);
					if (domicilioCompleto == null || "".equals(domicilioCompleto))
						domicilioCompleto = req.getDomicilio()+", COL "+req.getColoniaNombre()+", CP "+req.getCodigoPostal();

					solicitanteNuevo.setDomicilio(domicilioCompleto);

					Integer coloniaId = req.getColoniaId();

					if (coloniaId == null) {
						coloniaId = 0;
						coloniaId = obtenerColonia(ineOcer);
						if (coloniaId == null) {
							respuesta.setCode(-3);
							respuesta.setMenssage(
									"No se pudo realizar el registro, no se pudo obtener la colonia asociada al domicilio.");
							return respuesta;
						}
						
					} else if (coloniaId == 0)
						coloniaId = obtenerColonia(ineOcer);
					else if (coloniaId <= 0) {
						respuesta.setCode(-4);
						respuesta.setMenssage(
								"No se pudo realizar el registro, no se pudo obtener la colonia asociada al domicilio.");
						return respuesta;
					}
					
					
					
					solicitanteNuevo.setColonia(coloniaId);
					solicitanteNuevo.setCelular(req.getCelular());
					solicitanteNuevo.setTelefono("");
					if("H".equals(ineOcer.getSexo())) {
						solicitanteNuevo.setSexo("M");
					}else if("M".equals(ineOcer.getSexo())) {
						solicitanteNuevo.setSexo("F");
					}else {
						solicitanteNuevo.setSexo("");
					}
					
					solicitanteNuevo.setEdoCivil("S");

					String rfc1 = "";
					String rfc2 = "";
					String rfc3 = "";
					try {
						rfc1 = req.getRfc().substring(0, 4);
						rfc2 = req.getRfc().substring(4, 10);
						rfc3 = req.getRfc().substring(10, 13);
					} catch (Exception e) {
						/* Nada por hacer */}

					solicitanteNuevo.setRfc1(rfc1);
					solicitanteNuevo.setRfc2(rfc2);
					solicitanteNuevo.setRfc3(rfc3);

					solicitanteNuevo.setCurp(ineOcer.getCurp());
					solicitanteNuevo.setTPersona("F");
					solicitanteNuevo.setCorreo(req.getEmail());
					solicitanteNuevo.setNumeroCasa("");

					Integer edoNacId = obtenerEdoNac(ineOcer.getCurp());
					solicitanteNuevo.setEdoNacId(edoNacId);
					solicitanteNuevo.setCreadoPor(getUsuarioId()); /////// se
																	/////// debe
																	/////// obtener
																	/////// de
																	/////// un
																	/////// properties

					solicitanteNuevo.setNumero(sdao.nuevoSolicitante(solicitanteNuevo));

					numeroSolicitanteNuevo = solicitanteNuevo.getNumero();
					log.info("### req.getCodigoPromocion() :: " + req.getCodigoPromocion());
					if (req.getCodigoPromocion() != null) {
						if (!req.getCodigoPromocion().isEmpty()) {
							setComoEnteroId(35);
						}
					}

					ahorroContratoNuevo = new AhorroContrato();
					ahorroContratoNuevo = llenaDatosAhorro(solicitanteNuevo);
					if (ahorroContratoNuevo != null) {
						ahorroContratoNuevo.setAhorroContratoId(registraNuevoAhorroContrato(ahorroContratoNuevo));
						if (ahorroContratoNuevo.getAhorroContratoId() > 0) {
							if (req.getCodigoPromocion() != null) {
								if (!req.getCodigoPromocion().isEmpty()) {
									AhorroContratoDatos ahorroContratoDatos = new AhorroContratoDatos();
									ahorroContratoDatos.setCuenta(ahorroContratoNuevo.getCuenta());
									ahorroContratoDatos.setComoEnteroDesc(req.getCodigoPromocion());
									ahorroContratoDatos.setUsuarioCreacion(getUsuarioId());
									ahorroContratoDatos.setId(acddao.nuevoAhorroContratoDatos(ahorroContratoDatos));
									if (ahorroContratoDatos.getId() > 0) {
										log.info("### AHORRO CONTRATO DATOS REGISTRADO :: "
												+ ahorroContratoDatos.getId());
									}
									
								}
							}
							log.info("######VAR: "+ahorroContratoNuevo);
							log.info("######VAR2: "+req.getIneValidado());
							log.info("######VAR3: "+ getUsuarioId());
							log.info("######VAR4: "+ ConceptosUtil.VAL_OCR_INE);
							acdao.registrarConcepto(ahorroContratoNuevo.getAhorroContratoId(),
									ConceptosUtil.VAL_OCR_INE, req.getIneValidado().toString(), getUsuarioId());
							cuentaAhorroNueva = ahorroContratoNuevo.getCuenta();
							ahorroCopiaRendimientos(ahorroContratoNuevo);
							actualizaAhorroRendimientoVigente(ahorroContratoNuevo);
							String cuentaClabe = generaCuentaClabe(ahorroContratoNuevo);
							if (cuentaClabe != null) {
								if (!cuentaClabe.isEmpty()) {
									ahorroContratoNuevo.setCuentaClabe(cuentaClabe);
									actualizaCuentaClabe(ahorroContratoNuevo);
								} else {
									adao.borraAhorroContrato(ahorroContratoNuevo.getAhorroContratoId());

									respuesta.setCode(-10);
									respuesta.setMenssage("No se pudo realizar el registro");
									return respuesta;
								}
							} else {
								adao.borraAhorroContrato(ahorroContratoNuevo.getAhorroContratoId());

								respuesta.setCode(-9);
								respuesta.setMenssage("No se pudo realizar el registro");
								return respuesta;
							}

							Double gat = calculaGat(ahorroContratoNuevo.getCuenta());
							if (gat != null) {
								ahorroContratoNuevo.setGat(gat);
							} else {
								ahorroContratoNuevo.setGat((double) 0);
							}
							actualizaGat(ahorroContratoNuevo);

							ahorroSaldosNuevo = new AhorroSaldos();
							ahorroSaldosNuevo = llenaDatosAhorroSaldos(ahorroContratoNuevo);
							if (ahorroSaldosNuevo != null) {
								ahorroSaldosNuevo.setAhorroSaldosId(registraAhorroSaldosNuevo(ahorroSaldosNuevo));
								if (ahorroSaldosNuevo.getAhorroSaldosId() == 0) {
									adao.borraAhorroContrato(ahorroContratoNuevo.getAhorroContratoId());

									respuesta.setCode(-7);
									respuesta.setMenssage("No se pudo realizar el registro");
									return respuesta;
								}
							}

							DirectorioTelefonicoOBJ telefonoCoDi = new DirectorioTelefonicoOBJ();
							/*
							 * Boolean nuevoTelefonoCodi = true; telefonoCoDi
							 * =dtdao.obtenerTelfonoCelularCoDi(solicitanteNuevo
							 * .getNumero()); if(telefonoCoDi != null) {
							 * if(telefonoCoDi.getIdDirectorioTelefonico() !=
							 * null) {
							 * if(telefonoCoDi.getIdDirectorioTelefonico() > 0)
							 * { nuevoTelefonoCodi = false; System.out.
							 * println("## Ya tiene directorio telefonico"); } }
							 * }
							 * 
							 * if(nuevoTelefonoCodi) { System.out.
							 * println("## Crea directorio telefonico nuevo");
							 * telefonoCoDi =
							 * llenaDatosDirectorioTelefonico(solicitanteNuevo);
							 * telefonoCoDi.setIdDirectorioTelefonico(dtdao.
							 * nuevoDirectorioTelefonico( telefonoCoDi)); }else
							 * { System.out.
							 * println("## Actualiza directorio telefonico");
							 * telefonoCoDi.setTelefono(req.getCelular());
							 * telefonoCoDi.setModificadoPor(getUsuarioId());
							 * dtdao.actualizaDirectorioTelefonico(telefonoCoDi)
							 * ; }
							 */

							log.info("## Crea directorio telefonico nuevo");
							telefonoCoDi = llenaDatosDirectorioTelefonico(solicitanteNuevo);
							telefonoCoDi.setIdDirectorioTelefonico(dtdao.nuevoDirectorioTelefonico(telefonoCoDi));
						} else {
							respuesta.setCode(-4);
							respuesta.setMenssage("No se pudo realizar el registro");
							return respuesta;
						}
					} else {
						respuesta.setCode(-2);
						respuesta.setMenssage("No se pudo realizar el registro");
						return respuesta;
					}
				} else {
					log.info("## Soliciante ya existente");
					// SECCION PARA ACTUALIZAR DATOS
					List<AhorroContrato> ahorroExistenteList = new ArrayList<AhorroContrato>();
					ahorroExistenteList = adao.buscarCuentasXClienteTipoAhorro(solicitanteExistente.getNumero(),
							getProductoId());

					if (ahorroExistenteList != null) {
						if (ahorroExistenteList.size() > 0) {
							respuesta.setCode(-5);
							respuesta.setMenssage(
									"No se pudo realizar el registro, ya existe una cuenta de ahorro asociada a esta persona.");
							return respuesta;
						}
					}

					nombreSolicitante = solicitanteExistente.getNombre();

					solicitanteExistente.setCorreo(req.getEmail());
					solicitanteExistente.setCelular(req.getCelular());
					sdao.actualizaSolicitante(solicitanteExistente);
					log.info("### req.getCodigoPromocion() :: " + req.getCodigoPromocion());
					if (req.getCodigoPromocion() != null) {
						if (!req.getCodigoPromocion().isEmpty()) {
							setComoEnteroId(35);
						}
					}

					ahorroContratoNuevo = new AhorroContrato();
					ahorroContratoNuevo = llenaDatosAhorro(solicitanteExistente);
					if (ahorroContratoNuevo != null) {
						ahorroContratoNuevo.setAhorroContratoId(registraNuevoAhorroContrato(ahorroContratoNuevo));
						if (ahorroContratoNuevo.getAhorroContratoId() > 0) {
							if (req.getCodigoPromocion() != null) {
								if (!req.getCodigoPromocion().isEmpty()) {
									AhorroContratoDatos ahorroContratoDatos = new AhorroContratoDatos();
									ahorroContratoDatos.setCuenta(ahorroContratoNuevo.getCuenta());
									ahorroContratoDatos.setComoEnteroDesc(req.getCodigoPromocion());
									ahorroContratoDatos.setUsuarioCreacion(getUsuarioId());
									ahorroContratoDatos.setId(acddao.nuevoAhorroContratoDatos(ahorroContratoDatos));
									if (ahorroContratoDatos.getId() > 0) {
										log.info("### AHORRO CONTRATO DATOS REGISTRADO :: "
												+ ahorroContratoDatos.getId());
									}
									
								}
							}
							
							acdao.registrarConcepto(ahorroContratoNuevo.getAhorroContratoId(),
									ConceptosUtil.VAL_OCR_INE, req.getIneValidado().toString(), getUsuarioId());

							cuentaAhorroNueva = ahorroContratoNuevo.getCuenta();
							ahorroCopiaRendimientos(ahorroContratoNuevo);
							actualizaAhorroRendimientoVigente(ahorroContratoNuevo);
							String cuentaClabe = generaCuentaClabe(ahorroContratoNuevo);
							if (cuentaClabe != null) {
								if (!cuentaClabe.isEmpty()) {
									ahorroContratoNuevo.setCuentaClabe(cuentaClabe);
									actualizaCuentaClabe(ahorroContratoNuevo);
								} else {
									adao.borraAhorroContrato(ahorroContratoNuevo.getAhorroContratoId());

									respuesta.setCode(-10);
									respuesta.setMenssage("No se pudo realizar el registro");
									return respuesta;
								}
							} else {
								adao.borraAhorroContrato(ahorroContratoNuevo.getAhorroContratoId());

								respuesta.setCode(-9);
								respuesta.setMenssage("No se pudo realizar el registro");
								return respuesta;
							}

							Double gat = calculaGat(ahorroContratoNuevo.getCuenta());
							if (gat != null) {
								ahorroContratoNuevo.setGat(gat);
							} else {
								ahorroContratoNuevo.setGat((double) 0);
							}
							actualizaGat(ahorroContratoNuevo);

							ahorroSaldosNuevo = new AhorroSaldos();
							ahorroSaldosNuevo = llenaDatosAhorroSaldos(ahorroContratoNuevo);
							if (ahorroSaldosNuevo != null) {
								ahorroSaldosNuevo.setAhorroSaldosId(registraAhorroSaldosNuevo(ahorroSaldosNuevo));
								if (ahorroSaldosNuevo.getAhorroSaldosId() == 0) {
									adao.borraAhorroContrato(ahorroContratoNuevo.getAhorroContratoId());

									respuesta.setCode(-8);
									respuesta.setMenssage("No se pudo realizar el registro");
									return respuesta;
								}
							}

							Boolean nuevoTelefonoCodi = true;
							DirectorioTelefonicoOBJ telefonoCoDi = new DirectorioTelefonicoOBJ();
							telefonoCoDi = dtdao.obtenerTelfonoCelularCoDi(solicitanteExistente.getNumero());
							log.info("#Telefono CoDi :: " + gson.toJson(telefonoCoDi));
							if (telefonoCoDi != null) {
								if (telefonoCoDi.getIdDirectorioTelefonico() != null) {
									if (telefonoCoDi.getIdDirectorioTelefonico() > 0) {
										nuevoTelefonoCodi = false;
									}
								}
							}

							if (nuevoTelefonoCodi) {
								log.info("## Crea directorio telefonico nuevo");
								telefonoCoDi = llenaDatosDirectorioTelefonico(solicitanteExistente);
								telefonoCoDi.setIdDirectorioTelefonico(dtdao.nuevoDirectorioTelefonico(telefonoCoDi));
							} else {
								log.info("## Actualiza directorio telefonico");
								telefonoCoDi.setTelefono(req.getCelular());
								telefonoCoDi.setModificadoPor(getUsuarioId());
								dtdao.actualizaDirectorioTelefonico(telefonoCoDi);
							}

						} else {
							respuesta.setCode(-6);
							respuesta.setMenssage("No se pudo realizar el registro");
							return respuesta;
						}
					} else {
						respuesta.setCode(-3);
						respuesta.setMenssage("No se pudo realizar el registro");
						return respuesta;
					}
				}

				if (fondeaCuenta) {
					realizaFondeoCuenta(ahorroContratoNuevo);
				}

				log.info("Fondo por codigo: " + fondeoCodigo);
				if (fondeoCodigo) {
					AhorroTransferenciaReqOBJ transReq = new AhorroTransferenciaReqOBJ();

					transReq.setCuentaDestino(ahorroContratoNuevo.getCuenta());
					transReq.setCuentaOrigen(cam.getCuentaAhorro());

					transReq.setFecha(new Date());
					transReq.setMonto(cam.getIncentivo());

					transReq.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);

					transReq.setConceptoDestino(cam.getCampania() + "-" + (cam.getAcumulados() + 1));
					transReq.setConceptoOrigen(cam.getCampania() + "-" + (cam.getAcumulados() + 1));

					Respuesta r = trans.procesaTransferencia(transReq);

					if (r.getCodigo() == 0)
						cpdao.actualizarAcumulados(cam.getAcumulados() + 1, cam.getCampania());

				}

				respuesta.setCode(0);
				respuesta.setMenssage("cuenta de ahorro registrada");

				// Si no se realizaron validaciones de los datos del cliente
				// Registrar la cuenta en la tabla
				// solicitante_cuenta_simplificada
				if (!validaInformacion) {
					log.info("### REGISTRANDO LA CUENTA SIMPLIFICADA");
					Boolean res = sdao.registrarSolicitanteCuentaSimplificada(numeroSolicitanteNuevo);
					if (res == null || res == false) {
						log.info(
								"### NO SE PUDO REGISTRAR LA CUENTA SIMPLIFICADA EN LA TABLA solicitante_cuenta_simplificada");
					}
				}

				GeneraReporteTarjetaReq reqTarjetaAhorro = new GeneraReporteTarjetaReq();
				reqTarjetaAhorro.setMailFrom(REMITENTE_NOTIFICACIONES);
				reqTarjetaAhorro.setMailTo(cuentaCorreo);
				reqTarjetaAhorro.setSubject(ASUNTO_CORREO_ANEXO);
				//reqTarjetaAhorro.setMailBody(generaCuerpoCorreo(nombreSolicitante, cuentaAhorroNueva));
				reqTarjetaAhorro.setMailBody(generaNuevoCuerpoCorreo());
				reqTarjetaAhorro.setCuentaAhorro(cuentaAhorroNueva);
				reqTarjetaAhorro.setAccesoId(adao.generaTarjetaAhorro(cuentaAhorroNueva));

				GeneraReporteContratoReq contratoReq = new GeneraReporteContratoReq();
				contratoReq.setCuentaAhorro(cuentaAhorroNueva);
				contratoReq.setMailFrom(REMITENTE_NOTIFICACIONES);
				contratoReq.setMailTo(cuentaCorreo);
				contratoReq.setSubject(ASUNTO_CORREO_CONTRATO);
				contratoReq.setMailBody(
						generaCuerpoCorreoContratoCuentaSimplificada(nombreSolicitante, cuentaAhorroNueva));
				
				GeneraContratoServElecReq servElecReq = new GeneraContratoServElecReq();
				servElecReq.setCuenta(cuentaAhorroNueva);
				
				GeneraRegistroContratoReq regContrato = new GeneraRegistroContratoReq();
				regContrato.setCuenta(cuentaAhorroNueva);
				
				GeneraDisposicionesLegales regDispLeg = new GeneraDisposicionesLegales();
				regDispLeg.setCuenta(cuentaAhorroNueva);

				String folioContrato = "";
				if (reqTarjetaAhorro.getAccesoId() > 0) {
					log.info("Acceso ID :: " + reqTarjetaAhorro.getAccesoId());
					String numeroSolicitante = new String(ahorroContratoNuevo.getSolicitante());
					String numeroCuenta = new String (ahorroContratoNuevo.getCuenta());
					String rutaAlfresco = new String ("Alfresco/Personas/" + numeroSolicitante + "/Ahorro/" + numeroCuenta);
					String contrato = new String (ahorroContratoNuevo.getContrato());
					
					
					Executors.newSingleThreadExecutor().execute(new Runnable() {
						@Override
						public void run() {
							String nombre = new String();
							Respuesta respArrayBytes = new Respuesta();
							respArrayBytes = enviaNotificacionTarjetaYContratoMail(reqTarjetaAhorro, contratoReq,servElecReq, regContrato, regDispLeg);
							Type listType = new TypeToken<List<byte[]>>() {
				            }.getType();
				            String[] nombreArchivos = new String[5];
				            nombreArchivos[0]= "pdfBytesTarjeta.pdf";
				            nombreArchivos[1]= "pdfBytesContrato.pdf";
				            nombreArchivos[2]= "pdfBytesServElec.pdf";
				            nombreArchivos[3]= "pdfBytesRegCont.pdf";
				            nombreArchivos[4]= "pdfBytesDispLeg.pdf";
							ArrayList<byte[]> arregloBytes = new Gson().fromJson(respArrayBytes.getData(), listType);
							for(int x=0; x<arregloBytes.size(); x++) {
								int idDocumento = 0;
								String[] arregloArchivo = invokeServiceUploadImages(arregloBytes.get(x), nombreArchivos[x], numeroSolicitante, numeroCuenta);
								
								switch (x) {
									case 0:
										idDocumento = 1;
										nombre = "TARJETA DE DEPOSITO";
									break;
									
									case 1:
										idDocumento = 2;
										nombre = "CONTRATO";
									break;
									
									case 2:
										idDocumento = 3;
										nombre = "CONTRATO DE SERVICIOS ELETRONICOS";
									break;
									
									case 3:
										idDocumento = 4;
										nombre = "CARATULA";
									break;
									
									case 4:
										idDocumento = 5;
										nombre = "ANEXO DISPOSICIONES LEGALES";
									break;
									
									default:
										log.info("Fallo el switch de idDocumento");
									break;
								}
								
								AhorroAlfrescoOBJ objeto = LlenadoCamposBDAlfresco(numeroCuenta, idDocumento, rutaAlfresco, arregloArchivo, nombre);
								String obj = gson.toJson(objeto);
								OkHttpClient client = new OkHttpClient();
					            String auth = Credentials.basic("ASP", "a5p2017$");
					            MediaType media = MediaType.parse("application/json; charset=utf-8");
					            Request request;
					            Response resp;
					            String host = ConstantesUtil.WS_CERO_AHORRO+"/CuentaAhorroBDAlfresco";

					            request = new Request.Builder().url(host).post(RequestBody.create(media, obj)).header("Authorization", auth).build();
					            try {
									resp = client.newCall(request).execute();
									String porm = resp.body().string();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					            log.info("###PORM " + porm);
					            Respuesta resp2 = gson.fromJson(porm, Respuesta.class);
								
							}//end FOR
						}
					});

					folioContrato = generarRegistroCodi(cuentaAhorroNueva, numeroSolicitanteNuevo, req.getCelular());

					generarPIN(cuentaAhorroNueva, ahorroContratoNuevo.getReferencia(), nombreSolicitante, cuentaCorreo,
							req.getHeader().getIdCanalAtencion());
					
					generarNotificacionCallCenter(nombreSolicitante, ahorroContratoNuevo.getCuenta(), ahorroContratoNuevo.getSolicitante(),ConceptosUtil.PRODUCTO_CUENTA_FACIL);

					String cuenta = cuentaAhorroNueva;
					String solicitante = ahorroContratoNuevo.getSolicitante();
					/*
					 * Executors.newSingleThreadExecutor().execute(new
					 * Runnable() {
					 * 
					 * @Override public void run() {
					 * log.info("Inicia uploadIneAlfresco");
					 * uploadIneAlfresco(req.getValidacionOcrReq(), solicitante,
					 * cuenta); } });
					 */
				}

				AhorroCuentaOBJ ac = new AhorroCuentaOBJ();
				ac.setCuenta(cuentaAhorroNueva);
				ac.setClabe(ahorroContratoNuevo.getCuentaClabe());
				ac.setFolioContrato(folioContrato);
				respuesta.setData(gson.toJson(ac));
			} else {
				respuesta.setCode(-1);
				respuesta.setMenssage("No se pudo validar la informacion de la credencial INE/IFE");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("### TERMINO EL REGISTRO DE LA CUENTA DE MANERA CORRECTA");
		return respuesta;
	}

	private AhorroAlfrescoOBJ LlenadoCamposBDAlfresco(String contrato, int idDocumento, String carpeta, String[] arregloArchivo, String nombre) {
		AhorroAlfrescoOBJ obj= new AhorroAlfrescoOBJ();
		
		obj.setCuenta(contrato);
		obj.setDocumentos_ahorro_id(idDocumento);
		obj.setRuta_alfresco(carpeta);
		obj.setId_archivo_alfresco(arregloArchivo[0]);
		obj.setObservaciones("");
		obj.setNombre(nombre);
		obj.setFecha_expedicion(null);
		obj.setFecha_vigencia(null);
		return obj;
	}
	
	protected void uploadIneAlfresco(String validacionOcrReq, String numeroSolicitante, String cuenta) {
		try {
			String nombreArchivo = "";
			if (validacionOcrReq == null) {
				log.info("validacionOcrReq es null");
			} else if (validacionOcrReq.isEmpty()) {
				log.info("validacionOcrReq esta vacio");
			} else {
				// log.info("validacionOcrReq :: " +
				// validacionOcrReq);
			}

			ValidacionOcrReq validacionOcrObj = new ValidacionOcrReq();
			validacionOcrObj = gson.fromJson(validacionOcrReq, ValidacionOcrReq.class);
			if (validacionOcrObj != null) {
				if (validacionOcrObj.getId() != null) {
					if (!validacionOcrObj.getId().isEmpty()) {
						log.info("Inicia procesamiento frontal ine");
						nombreArchivo = "IneFrontal.png";
						byte[] imgBytes = Base64.getDecoder()
								.decode(new String(validacionOcrObj.getId()).getBytes("UTF-8"));
						;
						if (imgBytes != null) {
							log.info("Creo el arreglo de bytes de IneFrontal :: " + imgBytes.length);
							invokeServiceUploadImages(imgBytes, nombreArchivo, numeroSolicitante, cuenta);
						}
					} else {
						log.info("validacionOcrObj.id esta vaciol");
					}
				} else {
					log.info("validacionOcrObj.id es null");
				}

				if (validacionOcrObj.getIdReverso() != null) {
					if (!validacionOcrObj.getIdReverso().isEmpty()) {
						log.info("Inicia procesamiento reverso ine");
						nombreArchivo = "IneReverso.png";
						byte[] imgReversoBytes = Base64.getDecoder()
								.decode(new String(validacionOcrObj.getIdReverso()).getBytes("UTF-8"));
						;
						if (imgReversoBytes != null) {
							log.info("Creo el arreglo de bytes de IneReverso :: " + imgReversoBytes.length);
							invokeServiceUploadImages(imgReversoBytes, nombreArchivo, numeroSolicitante, cuenta);
						}
					} else {
						log.info("validacionOcrObj.idReverso esta vacio");
					}
				} else {
					log.info("validacionOcrObj.idReverso es null");
				}
			} else {
				log.info("validacionOcrObj es null");
			}
		} catch (Exception e) {
			log.error("ERROR AL GENERAR REPORTE");
			e.printStackTrace();

		}
	}

	private String[] invokeServiceUploadImages(byte[] imgBytes, String nombreArchivo, String numeroSolicitante,
			String cuentaAhorro) {
		String[] arregloArchivo = new String[2]; 
		try {
			
			String idArchivo = "";
			String archivo = "";

			String nameFolder = callCrearCarpeta(requestParametersCreateFolder(numeroSolicitante, cuentaAhorro));
			log.info("nameFolder :: " + nameFolder);
			ImagenAlfrescoReq req = requestParametersUploadImage(imgBytes, nombreArchivo, nameFolder);

			if (req != null) {
				RespuestaCommons response = callUploadFileImage(req);
				if (response.getImagenAlfresco() != null) {
					if (response.getImagenAlfresco().getIdImagen() != null) {
						idArchivo = response.getImagenAlfresco().getIdImagen();
						arregloArchivo[0] = idArchivo;
					} else {
						log.info("response.getImagenAlfresco().getIdImagen() null");
					}

					if (response.getImagenAlfresco().getNombre() != null) {
						archivo = response.getImagenAlfresco().getNombre();
						arregloArchivo[1] = archivo;
					} else {
						log.info("response.getImagenAlfresco().getNombre() null");
					}
					log.info("Se cargo el archivo exitosamente");

				} else {
					log.info("Ocurrio un error al subir archivo");
				}
			} else {
				log.info("Ocurrio un error al subir archivo ImagenAlfrescoReq null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arregloArchivo;
	}

	private String callCrearCarpeta(ImagenAlfrescoReq requestParameters) {
		RespuestaCommons response;

		RestCall2<ImagenAlfrescoReq, RespuestaCommons> callServiceAlfresco = new RestCall2<>();

		callServiceAlfresco.setUrl(ConstantesUtil.ALFRESCO_WS + "/WSAlfrescoREST" + "/data" + "/CrearCarpeta");
		if (callServiceAlfresco.getAuth() == null)
			callServiceAlfresco.setAuth(headerAuth);
		callServiceAlfresco.setEntrada(requestParameters);
		callServiceAlfresco.setClase(RespuestaCommons.class);
		response = callServiceAlfresco.call();
		return requestParameters.getCarpeta();
	}

	private final ImagenAlfrescoReq requestParametersCreateFolder(String numeroSolicitante, String cuentaAhorro) {
		ImagenAlfrescoReq request = new ImagenAlfrescoReq();
		request.setCarpeta("Personas/" + numeroSolicitante + "/Ahorro/" + cuentaAhorro);
		return request;
	}

	private RespuestaCommons callUploadFileImage(ImagenAlfrescoReq requestParameters) {
		RespuestaCommons response;
		RestCall2<ImagenAlfrescoReq, RespuestaCommons> peticion = new RestCall2<>();
		peticion.setUrl(ConstantesUtil.ALFRESCO_WS + "/WSAlfrescoREST" + "/data" + "/SubirImagenes");
		log.info("alfresco url :: " + peticion.getUrl());
		if (peticion.getAuth() == null)
			peticion.setAuth(headerAuth);
		peticion.setEntrada(requestParameters);
		peticion.setClase(RespuestaCommons.class);
		response = peticion.call();
		if (response == null) {
			log.info("Response alfresco null");
		} else {
			log.info("Response alfresco no es null");
			// log.info("Response alfresco :: " +
			// gson.toJson(response));
		}
		return response;
	}

	private final ImagenAlfrescoReq requestParametersUploadImage(byte[] bytesArray, String nameImage, String folder) {
		ImagenAlfrescoReq requestParameters = new ImagenAlfrescoReq();
		try {
			requestParameters.setFile(bytesArray);
			requestParameters.setCarpeta(folder);
			requestParameters.setNombreImagen(nameImage);
			// ruta += folder;
			// archivo = nameImage;
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return requestParameters;
	}

	// ###############################################################################################################################################################################
	private void realizaFondeoCuenta(AhorroContrato ahorroContratoNuevo) {
		log.info("##INICIA FONDEO DE CUENTA");
		Respuesta resp = new Respuesta();
		String resultadoFondeo = "";
		String numeroCuentaOrigenFondeo = "0020015500";
		Double montoFondeo = (double) 0;
		Boolean continuaFondeo = true;
		try {
			montoFondeo = pgdao.montoFondeoCuentaSimplificada();
			numeroCuentaOrigenFondeo = pgdao.cuentaOrigenFondeoCuentaSimplificada();
			if (numeroCuentaOrigenFondeo != null) {
				if (!numeroCuentaOrigenFondeo.isEmpty()) {
					if (montoFondeo != null) {
						if (montoFondeo > 0) {
							continuaFondeo = true;
						} else {
							continuaFondeo = false;
							resultadoFondeo = "FONDEO REALIZADO DE MANERA INCORRECTA :: NO SE PUDO OBTENER EL MONTO DEL FONDEO :: MENOR A CERO";
						}
					} else {
						continuaFondeo = false;
						resultadoFondeo = "FONDEO REALIZADO DE MANERA INCORRECTA :: NO SE PUDO OBTENER EL MONTO DEL FONDEO :: NULL";
					}
				} else {
					continuaFondeo = false;
					resultadoFondeo = "FONDEO REALIZADO DE MANERA INCORRECTA :: NO SE PUDO OBTENER EL NUMERO DE CUENTA DE ORIGEN DEL FONDEO :: VACIO";
				}
			} else {
				continuaFondeo = false;
				resultadoFondeo = "FONDEO REALIZADO DE MANERA INCORRECTA :: NO SE PUDO OBTENER EL NUMERO DE CUENTA DE ORIGEN DEL FONDEO :: NULL";
			}

			if (continuaFondeo) {
				AhorroTransferenciaReqOBJ transeferenciaReq = new AhorroTransferenciaReqOBJ();
				transeferenciaReq = llenaDatoMovimiento(numeroCuentaOrigenFondeo, ahorroContratoNuevo.getCuenta(),
						montoFondeo);
				resp = ahorroTransferenciaLogic.procesaTransferencia(transeferenciaReq);
				if (resp.getCodigo() == 0) {
					resultadoFondeo = "FONDEO REALIZADO DE MANERA CORRECTA";
				} else {
					resultadoFondeo = "FONDEO REALIZADO DE MANERA INCORRECTA :: " + resp.getMensaje();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultadoFondeo = "SE PRESENTARON PROBLEMAS EN EL FONDEO ::: " + e.getMessage();
		}
		log.info("##FINALIZO FONDEO DE CUENTA - " + resultadoFondeo);
	}

	private AhorroTransferenciaReqOBJ llenaDatoMovimiento(String numeroCuentaOrigen, String numeroCuentaDestino,
			Double monto) {
		AhorroTransferenciaReqOBJ resultado = new AhorroTransferenciaReqOBJ();
		AhorroContrato cuentaOrigen = new AhorroContrato();
		AhorroContrato cuentaDestino = new AhorroContrato();

		try {
			cuentaOrigen = adao.buscarByCuenta(numeroCuentaOrigen);
			cuentaDestino = adao.buscarByCuenta(numeroCuentaDestino);

			NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

			String conceptoOrigen = "FONDEO DE CUENTA: " + numeroCuentaDestino + " PARA USO CODI";

			String conceptoDestino = "BONO PARA USO DE APP PAGA FACIL";

			resultado.setConceptoOrigen(conceptoOrigen);
			resultado.setConceptoDestino(conceptoDestino);
			resultado.setCuentaOrigen(cuentaOrigen.getCuenta());
			resultado.setCuentaDestino(cuentaDestino.getCuenta());
			resultado.setFecha(Calendar.getInstance().getTime());
			resultado.setMonto(monto);
			resultado.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}

	private DirectorioTelefonicoOBJ llenaDatosDirectorioTelefonico(Solicitante solicitanteExistente) {
		DirectorioTelefonicoOBJ telefonoCoDi = new DirectorioTelefonicoOBJ();
		try {
			telefonoCoDi.setIdSolicitante(solicitanteExistente.getNumero());
			telefonoCoDi.setIdCatTelefono(getIdCatTelefono());
			telefonoCoDi.setTelefono(solicitanteExistente.getCelular());
			telefonoCoDi.setObservaciones(getObservacionDirTel());
			telefonoCoDi.setCreadoPor(getUsuarioId());
			return telefonoCoDi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void actualizaGat(AhorroContrato ahorroContratoNuevo) {
		try {
			adao.actualizaGatAhorroContrato(ahorroContratoNuevo.getGat(), ahorroContratoNuevo.getCuenta());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Double calculaGat(String cuenta) {
		Double gat = (double) 0;
		try {
			gat = adao.calculoGatByCuenta(cuenta);
			return gat;
		} catch (Exception e) {
			e.printStackTrace();
			return (double) 0;
		}
	}

	private void actualizaCuentaClabe(AhorroContrato ahorroContratoNuevo) {
		try {
			adao.actualizaCuentaClabe(ahorroContratoNuevo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String generaCuentaClabe(AhorroContrato ahorroContratoNuevo) {
		try {
			GenerarClabeLogic generaClabe = new GenerarClabeLogic();
			String cuentaClabe = "";
			cuentaClabe = generaClabe.generarClabe(ahorroContratoNuevo.getReferencia(),
					ahorroContratoNuevo.getTipoAhorroId(), ahorroContratoNuevo.getSucursalApertura());
			return cuentaClabe;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void actualizaAhorroRendimientoVigente(AhorroContrato ahorroContratoNuevo) {
		try {
			adao.actualizaAhorroRendimientosVigntes(getUsuarioId(), getTipoCapitalizarId(),
					ahorroContratoNuevo.getCuenta());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void ahorroCopiaRendimientos(AhorroContrato ahorroContratoNuevo) {
		try {
			String result = adao.ahorroCopiaRendimientos(ahorroContratoNuevo.getCuenta(),
					ahorroContratoNuevo.getRendimientoId(), (double) 0, getUsuarioId(),
					ahorroContratoNuevo.getMontoApertura());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Integer registraAhorroSaldosNuevo(AhorroSaldos ahorroSaldosNuevo) {
		try {
			ahorroSaldosNuevo.setAhorroSaldosId(asdao.nuevo(ahorroSaldosNuevo));
			return ahorroSaldosNuevo.getAhorroSaldosId();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private AhorroSaldos llenaDatosAhorroSaldos(AhorroContrato ahorroContratoNuevo) {
		try {
			AhorroSaldos ahorroSaldosNuevo = new AhorroSaldos();
			ahorroSaldosNuevo.setCuenta(ahorroContratoNuevo.getCuenta());
			ahorroSaldosNuevo.setSolicitanteId(ahorroContratoNuevo.getSolicitante());
			ahorroSaldosNuevo.setSaldoReal((double) 0);
			ahorroSaldosNuevo.setSaldoPromedio((double) 0);
			ahorroSaldosNuevo.setSaldoAcumulado((double) 0);
			ahorroSaldosNuevo.setFechaCorte(Calendar.getInstance().getTime());
			ahorroSaldosNuevo.setFechaDeposito(Calendar.getInstance().getTime());
			ahorroSaldosNuevo.setIntereses((double) 0);
			ahorroSaldosNuevo.setIva((double) 0);
			ahorroSaldosNuevo.setIsr((double) 0);
			ahorroSaldosNuevo.setRetenciones((double) 0);
			ahorroSaldosNuevo.setDesviacion((double) 0);
			ahorroSaldosNuevo.setDias(0);
			ahorroSaldosNuevo.setSaldoDisponible((double) 0);
			ahorroSaldosNuevo.setIde((double) 0);

			return ahorroSaldosNuevo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Integer registraNuevoAhorroContrato(AhorroContrato ahorroContratoNuevo) {
		try {
			log.info("## Datos de ahorro a registrar :: " + gson.toJson(ahorroContratoNuevo));
			ahorroContratoNuevo.setAhorroContratoId(adao.nuevoAhorroContrato(ahorroContratoNuevo));
			return ahorroContratoNuevo.getAhorroContratoId();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	private AhorroContrato llenaDatosAhorro(Solicitante solicitante) {
		AhorroContrato ahorroContratoNuevo = new AhorroContrato();
		try {
			log.info("# Datos solicitante :: " + gson.toJson(solicitante));

			String cuenta = adao.obtenerSecuenciaCuenta();
			log.info("# Secuencia de la cuenta :: " + cuenta);
			if (cuenta == null) {
				return null;
			} else if (cuenta.isEmpty()) {
				return null;
			}

			
			log.info("# Obtiene sucursal de la colonia:" + solicitante.getColonia());
			//Integer sucursalId = obtenerSucursalIdByColoniaId(solicitante.getColonia());
			
			Integer sucursalId = 0;
			
			log.info("# Sucursal id obtenido :: " + ConceptosUtil.SUCURSAL_VIRTUAL_CODI);
			sucursalId = ConceptosUtil.SUCURSAL_VIRTUAL_CODI;
			log.info("# Sucursal id obtenido :: " + sucursalId);
			
			if (sucursalId == null) {
				return null;
			} else if (sucursalId <= 0) {
				return null;
			}

			String secV = "00000" + cuenta;
			secV = secV.substring(secV.length() - 5, secV.length());
			String s = "000" + sucursalId;
			s = s.substring(s.length() - 3, s.length());
			secV = s + secV;
			secV = secV + "00";
			cuenta = secV;
			////// falta completar la secuencia
			log.info("# Cuenta clacula :: " + cuenta);
			ahorroContratoNuevo.setCuenta(cuenta);

			String sucursalApertura = obtenerSucursalAperturaByRegionId(sucursalId);
			log.info("#Sucursal de apertura obtenida :: " + sucursalApertura);
			if (sucursalApertura == null) {
				return null;
			} else if (sucursalApertura.isEmpty()) {
				return null;
			}
			
			String montoMaxAhorro=pgdao.montoMaxAhorroCuentaSimplificada();
			Double montoMaxUdis=0.0;
			Double valorUdi=vdao.obtenerValorUdi();
			
			if(montoMaxAhorro!=null && !montoMaxAhorro.equals(""))
				montoMaxUdis=Double.parseDouble(montoMaxAhorro);
			
			
			ahorroContratoNuevo.setSucursalApertura(sucursalApertura);

			ahorroContratoNuevo.setTipoAhorroId(getProductoId()); //////// tipo
																	//////// ahorro
																	//////// 2 =
																	//////// MI
																	//////// AHORRO
																	//////// ASP
																	//////// ///////////////
																	//////// 6 =
																	//////// MI
																	//////// CUENTA
																	//////// AHORRO
																	//////// SIMPLIFICADA
			ahorroContratoNuevo.setRendimientoId(getRendimientoId());
			ahorroContratoNuevo.setFechaApertura(Calendar.getInstance().getTime());
			ahorroContratoNuevo.setSolicitante(solicitante.getNumero());
			ahorroContratoNuevo.setTitularId(solicitante.getNumero());
			ahorroContratoNuevo.setMonedaId(1);

			// ahorroContratoNuevo.setAsociacionId(asociacionId);
			ahorroContratoNuevo.setDomicilio(solicitante.getDomicilio());
			ahorroContratoNuevo.setNumeroCasa(solicitante.getNumeroCasa());
			ahorroContratoNuevo.setColoniaId(solicitante.getColonia());

			String ctaContable = adao.obtenerCuentaContable(getRendimientoId(), getProductoId());
			log.info("#Cuenta contable obtenida del pl :: " + ctaContable);
			if (ctaContable == null) {
				return null;
			} else if (ctaContable.isEmpty()) {
				return null;
			}
			ahorroContratoNuevo.setCtaContable(ctaContable);

			ahorroContratoNuevo.setEstatus("V");
			ahorroContratoNuevo.setCreadoPor(getUsuarioId());

			String contrato = adao.obtenerSecuenciaContrato();
			log.info("#Secuencia de contrato obtenida :: " + contrato);
			if (contrato == null) {
				return null;
			} else if (ctaContable.isEmpty()) {
				return null;
			}
			
			String secV2 = "000000000" + contrato;
			secV2 = secV2.substring(secV2.length() - 10, secV2.length());
			contrato = secV2;
			////// falta completar la secuencia
			log.info("#Contrato calculado :: " + contrato);
			ahorroContratoNuevo.setContrato(contrato);

			ahorroContratoNuevo.setSaldo((double) 0);
			// ahorroContratoNuevo.setOficialId(oficialId);
			log.info("#Datos para calcular referencia");
			log.info("#Cuenta :: " + cuenta);
			log.info("#Usuario :: " + getUsuarioId());
			log.info("#Sucursal :: " + sucursalApertura);
			String referencia = adao.ahorroGeneraReferencia(cuenta, getUsuarioId(), sucursalApertura);
			log.info("#Referencia obtenida del pl :: " + referencia);
			if (referencia == null) {
				return null;
			} else if (referencia.isEmpty()) {
				return null;
			}
			ahorroContratoNuevo.setReferencia(referencia);

			ahorroContratoNuevo.setCuentaPadre(cuenta);
			ahorroContratoNuevo.setCuentaDestinoCap(cuenta);
			ahorroContratoNuevo.setCuentaDestinoRen(cuenta);
			ahorroContratoNuevo.setMontoApertura((double) 0);
			ahorroContratoNuevo.setCorreoEdocuenta(solicitante.getCorreo());
			ahorroContratoNuevo.setMontoMaxAhorro(montoMaxUdis*valorUdi);
			

			String cuentaClabe = "";
			ahorroContratoNuevo.setCuentaClabe(cuentaClabe);

			ahorroContratoNuevo.setIdComoEntero(getComoEnteroId());
			ahorroContratoNuevo.setErrores(0);
			ahorroContratoNuevo.setStatusBloqueo(0);
			ahorroContratoNuevo.setOficialId(getAcesorId());
			
			ahorroContratoNuevo.setActividadId(ConceptosUtil.ACTIVIDAD_ID);
			ahorroContratoNuevo.setGiroId(ConceptosUtil.GIRO_ID);
			ahorroContratoNuevo.setCveDestino(ConceptosUtil.CVE_DESTINO);
			ahorroContratoNuevo.setOcupacionId(ConceptosUtil.OCUPACION_ID);
			// ahorroContratoNuevo.setPin(pin);
			// ahorroContratoNuevo.setPinAuto(pinAuto);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ahorroContratoNuevo;
	}

	private Integer obtenerSucursalIdByColoniaId(Integer coloniaId) {
		RegionesOBJ region = new RegionesOBJ();
		try {
			region = regdao.obtenerSucursalIdByColoniaId(coloniaId);
			return region.getClave();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	private String obtenerSucursalAperturaByRegionId(Integer regionId) {
		String sucursalApertura = "";
		AgenteOBJ agente = new AgenteOBJ();
		try {
			agente = agdao.obtenerSucursalAperturaByRegion(regionId);
			sucursalApertura = agente.getClave();
			return sucursalApertura;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	private Integer obtenerEdoNac(String curp) {
		try {
			Integer edoNacId = 0;
			String cveCurp = curp.substring(11, 13);
			log.info("#Clave de estado del curp:" + cveCurp);
			edoNacId = sdao.obtenerEdoNacByCveCURP(cveCurp);
			log.info("#Estado obtenido de el curp:" + edoNacId);
			return edoNacId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Integer obtenerColonia(IneOcrRespOBJ ineOcer) {
		String cp = "";
		String nombreColonia = "";
		ColoniaOBJ colonia = new ColoniaOBJ();

		try {
			cp = ineOcer.getColonia().substring(ineOcer.getColonia().length() - 5, ineOcer.getColonia().length());
			nombreColonia = ineOcer.getColonia().substring(0, ineOcer.getColonia().length() - 6);
		} catch (Exception e) {
			// Nada por hacer
		}

		String coloniaNombre = nombreColonia;
		coloniaNombre = coloniaNombre.replace("COL ", "");
		coloniaNombre = coloniaNombre.replace("CONJ HAB  ", "");
		log.info("# coloniaNombre :: " + coloniaNombre);
		colonia = coldao.obtenerColoniaByCpNombre(cp, coloniaNombre);
		if (colonia != null) {
			if (colonia.getClave() != null) {
				if (colonia.getClave() > 0) {
					return colonia.getClave();
				}
			}
		}
		log.info("#No encontro la colonia: " + coloniaNombre + " Con el codigo postal: " + cp);

		if (ineOcer.getLocalidad() != null) {

			log.info("#Localidad con la que buscara colonia: " + Integer.valueOf(ineOcer.getLocalidad()));

			colonia = coldao.obtenerColoniaCentroByLocalidad(Integer.valueOf(ineOcer.getLocalidad()));
			if (colonia != null) {
				if (colonia.getClave() != null) {
					if (colonia.getClave() > 0) {
						return colonia.getClave();
					}
				}
			}
		}

		return 1; // Por default regrese colonia centro con clave 1
	}

	private String construirDomicilio(IneOcrRespOBJ ineOcer) {
		String domicilio = "";
		try {
			if (ineOcer.getCalle() == null && ineOcer.getColonia() == null && ineOcer.getCiudad() == null)
				domicilio = "";
			else
				domicilio = ineOcer.getCalle() + " " + ineOcer.getColonia() + " " + ineOcer.getCiudad();

			return domicilio;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String generaCuerpoCorreo(String nombre, String cuenta) {
		String body = "";
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\">"
				+ "<head>" + "<meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\">"
				+ "<meta name=Generator content=\"Microsoft Word 12 (filtered medium)\">" + "<style>" + "<!--"
				+ " @font-face" + "	{font-family:\"Cambria Math\";" + "	panose-1:2 4 5 3 5 4 6 3 2 4;}" + "@font-face"
				+ "	{font-family:Calibri;" + "	panose-1:2 15 5 2 2 2 4 3 2 4;}" + "@font-face"
				+ "	{font-family:Tahoma;" + "	panose-1:2 11 6 4 3 5 4 4 2 4;}" + "@font-face"
				+ "	{font-family:\"Century Gothic\";" + "	panose-1:2 11 5 2 2 2 2 2 2 4;}" + "@font-face"
				+ "	{font-family:\"Ubuntu Light\";" + "	panose-1:2 11 3 4 3 6 2 3 2 4;}"
				+ " p.MsoNormal, li.MsoNormal, div.MsoNormal" + "	{margin:0cm;" + "	margin-bottom:.0001pt;"
				+ "	font-size:11.0pt;" + "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}"
				+ "a:link, span.MsoHyperlink" + "	{mso-style-priority:99;" + "	color:#0563C1;"
				+ "	text-decoration:underline;}" + "a:visited, span.MsoHyperlinkFollowed" + "	{mso-style-priority:99;"
				+ "	color:#954F72;" + "	text-decoration:underline;}" + "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate"
				+ "	{mso-style-priority:99;" + "	mso-style-link:\"Texto de globo Car\";" + "	margin:0cm;"
				+ "	margin-bottom:.0001pt;" + "	font-size:8.0pt;" + "	font-family:\"Tahoma\",\"sans-serif\";"
				+ "	color:#3B547B;}" + "span.EstiloCorreo17" + "	{mso-style-type:personal-compose;"
				+ "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}" + "span.TextodegloboCar"
				+ "	{mso-style-name:\"Texto de globo Car\";" + "	mso-style-priority:99;"
				+ "	mso-style-link:\"Texto de globo\";" + "	font-family:\"Tahoma\",\"sans-serif\";" + "	color:#3B547B;}"
				+ ".MsoChpDefault" + "	{mso-style-type:export-only;}" + "@page Section1" + "	{size:612.0pt 792.0pt;"
				+ "	margin:70.85pt 3.0cm 70.85pt 3.0cm;}" + "div.Section1" + "	{page:Section1;}" + "-->" + "</style>"
				+ "<!--[if gte mso 9]><xml>" + "<o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" />"
				+ "</xml><![endif]--><!--[if gte mso 9]><xml>" + " <o:shapelayout v:ext=\"edit\">"
				+ "  <o:idmap v:ext=\"edit\" data=\"1\" />" + " </o:shapelayout></xml><![endif]-->" + "</head>"
				+ "<body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\">" + "<div class=Section1>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "<img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br />"
				+ "</span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado(a) <b>"
				+ nombre + "</b>:<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "&#161;Gracias por elegir ASP Integra Opciones&#33; Te compartimos tu tarjeta de ahorro que acabas de crear con la terminaci&#243;n de n&#250;mero de cuenta: ******"
				+ cuenta.substring(cuenta.length() - 4) + "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Los dep&#243;sitos a tu cuenta de ahorro podr&#225;s realizarlos a trav&#233;s de SPEI, usando tu CLABE INTERBANCARIA, "
				+ "en tu sucursal m&#225;s cercana o a trav&#233;s de la red de Ventanilla F&#225;cil.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Si deseas retirar efectivo solo acude a tu sucursal m&#225;s cercana o Ventanilla F&#225;cil con tu identificaci&#243;n oficial vigente."
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Recuerda que puedes realizar todas tus compras a trav&#233;s de CoDi sin cargo adicional. Desc&#225;rgala "
				+ "<a href=\"https://play.google.com/store/apps/details?id=com.codi.aspintegraopciones.aspcodi\" target=\"_blank\">aqu&#237;</a> y s&#250;mate a la nueva forma de pagar en M&#233;xico."
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><b><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Atentamente<o:p></o:p></span></b></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Opciones Empresariales del Noreste, S.A. de C.V. S.F.P.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en "
				+ "<a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a> "
				+ " o en tu Sucursal m&#225;s cercana." + "<o:p></o:p></span></p>"

				// +"<p class=MsoNormal><span lang=ES
				// style='font-family:\"Century
				// Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				// +"<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "Consulta los costos y comisiones de nuestros productos en "
				+ "<a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> "
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "</div>"
				+ "</body>" + "</html>";
		return body;
	}

	private String generaCuerpoCorreoContratoCuentaSimplificada(String nombre, String cuenta) {
		String body = "";
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\">"
				+ "<head>" + "<meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\">"
				+ "<meta name=Generator content=\"Microsoft Word 12 (filtered medium)\">" + "<style>" + "<!--"
				+ " @font-face" + "	{font-family:\"Cambria Math\";" + "	panose-1:2 4 5 3 5 4 6 3 2 4;}" + "@font-face"
				+ "	{font-family:Calibri;" + "	panose-1:2 15 5 2 2 2 4 3 2 4;}" + "@font-face"
				+ "	{font-family:Tahoma;" + "	panose-1:2 11 6 4 3 5 4 4 2 4;}" + "@font-face"
				+ "	{font-family:\"Century Gothic\";" + "	panose-1:2 11 5 2 2 2 2 2 2 4;}" + "@font-face"
				+ "	{font-family:\"Ubuntu Light\";" + "	panose-1:2 11 3 4 3 6 2 3 2 4;}"
				+ " p.MsoNormal, li.MsoNormal, div.MsoNormal" + "	{margin:0cm;" + "	margin-bottom:.0001pt;"
				+ "	font-size:11.0pt;" + "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}"
				+ "a:link, span.MsoHyperlink" + "	{mso-style-priority:99;" + "	color:#0563C1;"
				+ "	text-decoration:underline;}" + "a:visited, span.MsoHyperlinkFollowed" + "	{mso-style-priority:99;"
				+ "	color:#954F72;" + "	text-decoration:underline;}" + "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate"
				+ "	{mso-style-priority:99;" + "	mso-style-link:\"Texto de globo Car\";" + "	margin:0cm;"
				+ "	margin-bottom:.0001pt;" + "	font-size:8.0pt;" + "	font-family:\"Tahoma\",\"sans-serif\";"
				+ "	color:#3B547B;}" + "span.EstiloCorreo17" + "	{mso-style-type:personal-compose;"
				+ "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}" + "span.TextodegloboCar"
				+ "	{mso-style-name:\"Texto de globo Car\";" + "	mso-style-priority:99;"
				+ "	mso-style-link:\"Texto de globo\";" + "	font-family:\"Tahoma\",\"sans-serif\";" + "	color:#3B547B;}"
				+ ".MsoChpDefault" + "	{mso-style-type:export-only;}" + "@page Section1" + "	{size:612.0pt 792.0pt;"
				+ "	margin:70.85pt 3.0cm 70.85pt 3.0cm;}" + "div.Section1" + "	{page:Section1;}" + "-->" + "</style>"
				+ "<!--[if gte mso 9]><xml>" + "<o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" />"
				+ "</xml><![endif]--><!--[if gte mso 9]><xml>" + " <o:shapelayout v:ext=\"edit\">"
				+ "  <o:idmap v:ext=\"edit\" data=\"1\" />" + " </o:shapelayout></xml><![endif]-->" + "</head>"
				+ "<body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\">" + "<div class=Section1>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "<img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br />"
				+ "</span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado(a) <b>"
				+ nombre + "</b>:<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "&#161;Gracias por elegir ASP Integra Opciones&#33; Te compartimos tu contrato de ahorro que acabas de crear con la terminaci&#243;n de n&#250;mero de cuenta: ******"
				+ cuenta.substring(cuenta.length() - 4) + "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Los dep&#243;sitos a tu cuenta de ahorro podr&#225;s realizarlos a trav&#233;s de SPEI, usando tu CLABE INTERBANCARIA, "
				+ "en tu sucursal m&#225;s cercana o a trav&#233;s de la red de Ventanilla F&#225;cil.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Si deseas retirar efectivo solo acude a tu sucursal m&#225;s cercana o Ventanilla F&#225;cil con tu identificaci&#243;n oficial vigente."
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Recuerda que puedes realizar todas tus compras a trav&#233;s de CoDi sin cargo adicional. Desc&#225;rgala "
				+ "<a href=\"https://play.google.com/store/apps/details?id=com.codi.aspintegraopciones.aspcodi\" target=\"_blank\">aqu&#237;</a> y s&#250;mate a la nueva forma de pagar en M&#233;xico."
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><b><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Atentamente<o:p></o:p></span></b></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Opciones Empresariales del Noreste, S.A. de C.V. S.F.P.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en "
				+ "<a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a> "
				+ " o en tu Sucursal m&#225;s cercana." + "<o:p></o:p></span></p>"

				// +"<p class=MsoNormal><span lang=ES
				// style='font-family:\"Century
				// Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				// +"<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "Consulta los costos y comisiones de nuestros productos en "
				+ "<a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> "
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "</div>"
				+ "</body>" + "</html>";
		return body;
	}
	
	private String generaNuevoCuerpoCorreo() {
		String body = "";
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\"><meta name=Generator content=\"Microsoft Word 12 (filtered medium)\"><style><!-- @font-face	{font-family:\"Cambria Math\";	panose-1:2 4 5 3 5 4 6 3 2 4;}@font-face	{font-family:Calibri;	panose-1:2 15 5 2 2 2 4 3 2 4;}@font-face	{font-family:Tahoma;	panose-1:2 11 6 4 3 5 4 4 2 4;}@font-face	{font-family:\"Century Gothic\";	panose-1:2 11 5 2 2 2 2 2 2 4;}@font-face	{font-family:\"Ubuntu Light\";	panose-1:2 11 3 4 3 6 2 3 2 4;} p.MsoNormal, li.MsoNormal, div.MsoNormal	{margin:0cm;	margin-bottom:.0001pt;	font-size:11.0pt;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}a:link, span.MsoHyperlink	{mso-style-priority:99;	color:#0563C1;	text-decoration:underline;}a:visited, span.MsoHyperlinkFollowed	{mso-style-priority:99;	color:#954F72;	text-decoration:underline;}p.MsoAcetate, li.MsoAcetate, div.MsoAcetate	{mso-style-priority:99;	mso-style-link:\"Texto de globo Car\";	margin:0cm;	margin-bottom:.0001pt;	font-size:8.0pt;	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}span.EstiloCorreo17	{mso-style-type:personal-compose;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}span.TextodegloboCar	{mso-style-name:\"Texto de globo Car\";	mso-style-priority:99;	mso-style-link:\"Texto de globo\";	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}.MsoChpDefault	{mso-style-type:export-only;}@page Section1	{size:612.0pt 792.0pt;	margin:70.85pt 3.0cm 70.85pt 3.0cm;}div.Section1	{page:Section1;}--></style><!--[if gte mso 9]><xml><o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" /></xml><![endif]--><!--[if gte mso 9]><xml> <o:shapelayout v:ext=\"edit\">  <o:idmap v:ext=\"edit\" data=\"1\" /> </o:shapelayout></xml><![endif]--></head><body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\"><div class=Section1><p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'><img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br /></span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado Cliente;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&#161;Bienvenido a ASP Integra Opciones&#33;<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Ha realizado la apertura de su Cuenta F&#225;cil, en el presente correo se adjunta su Contrato de apertura de cuenta de Ahorro y su Tarjeta en donde podr&#225; visualizar su CLABE Interbancaria y los 10 d&#237;gitos de su cuenta.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Le Recordamos que s&#237; existen dudas sobre el clausulado en general de su contrato o el manejo de su cuenta, est&#225; disponible la l&#237;nea 800 462 73 73.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Siempre ser&#225; un gusto atenderle.<o:p></o:p>\r\n" + 
				"	</span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p>\r\n" + 
				"	</span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Por su atenci&#243;n, Gracias.<o:p></o:p>\r\n" + 
				"	</span></p><p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>ASP Integra Opciones.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en <a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a>  o en tu Sucursal m&#225;s cercana.<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>Consulta los costos y comisiones de nuestros productos en <a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> <o:p></o:p></span></p><p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p><p class=MsoNormal><o:p>&nbsp;</o:p></p><p class=MsoNormal><o:p>&nbsp;</o:p></p></div></body></html>";
		return body;
	}
	
	private static String generaBodyCorreoNotificacionCallCenter(String nombreCliente, String numeroCuenta, String numeroSolicitante, String producto) {
		String body = "";
		String productoFormateado = producto;
		productoFormateado = productoFormateado.replace("á", "&#225;");
		SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'a las' HH:mm a");
		Date fechaActual = Calendar.getInstance().getTime();
		String fecha = formato.format(fechaActual);
		//log.info("Fecha :: " + fecha);
		
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\">"
				+ "<meta name=Generator content=\"Microsoft Word 12 (filtered medium)\"><style><!-- @font-face	{font-family:\"Cambria Math\";	panose-1:2 4 5 3 5 4 6 3 2 4;}@font-face	{font-family:Calibri;	panose-1:2 15 5 2 2 2 4 3 2 4;}@font-face	{font-family:Tahoma;	panose-1:2 11 6 4 3 5 4 4 2 4;}@font-face	{font-family:\"Century Gothic\";	"
				+ "panose-1:2 11 5 2 2 2 2 2 2 4;}@font-face	{font-family:\"Ubuntu Light\";	panose-1:2 11 3 4 3 6 2 3 2 4;} p.MsoNormal, li.MsoNormal, div.MsoNormal	{margin:0cm;	margin-bottom:.0001pt;	font-size:11.0pt;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}a:link, span.MsoHyperlink	{mso-style-priority:99;	color:#0563C1;	"
				+ "text-decoration:underline;}a:visited, span.MsoHyperlinkFollowed	{mso-style-priority:99;	color:#954F72;	text-decoration:underline;}p.MsoAcetate, li.MsoAcetate, div.MsoAcetate	{mso-style-priority:99;	mso-style-link:\"Texto de globo Car\";	margin:0cm;	margin-bottom:.0001pt;	font-size:8.0pt;	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}span.EstiloCorreo17	"
				+ "{mso-style-type:personal-compose;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}span.TextodegloboCar	{mso-style-name:\"Texto de globo Car\";	mso-style-priority:99;	mso-style-link:\"Texto de globo\";	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}.MsoChpDefault	"
				+ "{mso-style-type:export-only;}@page Section1	{size:612.0pt 792.0pt;	margin:70.85pt 3.0cm 70.85pt 3.0cm;}div.Section1	{page:Section1;}--></style><!--[if gte mso 9]><xml><o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" /></xml><![endif]--><!--[if gte mso 9]>\r\n"
				+ "<xml>"
				+ "<o:shapelayout v:ext=\"edit\">  <o:idmap v:ext=\"edit\" data=\"1\" />" 
				+ "</o:shapelayout></xml><![endif]--></head><body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\"><div class=Section1><p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "<img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br /></span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado Ejecutivo, El d&#237;a " + fecha + " se realiz&#243; el tr&#225;mite de apertura del Cliente: " + nombreCliente + " Cuenta: " + numeroCuenta + " Producto: " + productoFormateado + "; favor de realizar la llamada de Validaci&#243;n de los datos.<o:p></o:p></span></p>"
				+ "<br /><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Favor de revisar su seguimiento en la Plataforma SISTACC y actualizar. El n&#250;mero de solicitante es " + numeroSolicitante + "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p>\r\n"
				+ "	</div></body></html>";
		
			log.info(body);
		
			//log.info(body);
		return body;
	}

	private Respuesta enviaNotificacionTarjetaYContratoMail(GeneraReporteTarjetaReq tarjeta,
			GeneraReporteContratoReq contrato,GeneraContratoServElecReq servElecReq, GeneraRegistroContratoReq regContrato, 
			GeneraDisposicionesLegales regDispleg) {
		Respuesta respArrayBytes = new Respuesta();
		try {
			GeneraNotificacionesMail generaNotificaciones = new GeneraNotificacionesMail();
			respArrayBytes= generaNotificaciones.enviaTarjetaAhorroYContrato(tarjeta, contrato,servElecReq, regContrato, regDispleg);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return respArrayBytes;
	}

	private void enviaTarjetaAhorro(GeneraReporteTarjetaReq req) {
		try {
			GeneraNotificacionesMail generaNotificaciones = new GeneraNotificacionesMail();
			generaNotificaciones.enviaTarjetaAhorro(req);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	protected void enviaContratoCuentaSimplificada(GeneraReporteContratoReq contratoReq) {
		try {
			GeneraNotificacionesMail generaNotificaciones = new GeneraNotificacionesMail();
			generaNotificaciones.enviaContratoCuentaSimplificada(contratoReq);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private String generarRegistroCodi(String cuenta, String numeroSolicitanteNuevo, String celular) {

		String folioContrato = "";
		Respuesta resContrato = generaContrato(cuenta, numeroSolicitanteNuevo);

		Respuesta resp = new Respuesta();
		RegistroServiciosDigitalesOBJ servicio = rsdao.buscarRegistroServiciosDigitales(cuenta);
		RegistroCodiOBJ codi = rcdao.buscarRegistroCodi(cuenta);

		// ReporteContratoServiciosElectronicosOBJ sol =
		// acDAO.consultarDatosClienteCuentaAhorro(cuenta);
		if (servicio != null && codi != null) {
			folioContrato = servicio.getFolioContrato();
			GuardarServiciosDigitalesReq req = new GuardarServiciosDigitalesReq();
			req = gson.fromJson(resContrato.getData(), GuardarServiciosDigitalesReq.class);

			servicio.setEstatus(2);
			servicio.setAlfrescoId(req.getServicios().getAlfrescoId());
			servicio.setNombreDocumento(req.getServicios().getNombreDocumento());
			servicio.setTipoArchivoId(40);

			String pass = servicio.getFolioContrato().substring(servicio.getFolioContrato().length() - 4)
					+ servicio.getCuenta().substring(servicio.getCuenta().length() - 4);

			String crypt = Encrypted.getSecurePassword(pass, servicio.getCuenta());

			servicio.setPassword(crypt);

			codi.setEstatus(2);

			rcdao.actualizarRegistroCodi(codi);
			rsdao.actualizarRegistroServiciosDigitales(servicio);

			resp.setCodigo(0);
			resp.setMensaje("OK");

			// Enviar el SMS
			SMS sms = new SMS();
			sms.setTransaccionId(12);
			sms.setCelular(celular);
			sms.setSolicitanteId(numeroSolicitanteNuevo);
			sms.setUsuarioId(9);
			sms.setCatTiposMensajesId(4);
			sms.setServiciosActivosId(4);
			sms.setOperacion("Notificación cuenta simplificada");
			sms.setIdAplicacion(20);
			sms.setDescripcion("Notificacion Alta cuenta simplificada");

			sms.setMensaje(
					"ASP INT OPC le comparte sus datos de ingreso para CoDi. Folio: " + servicio.getFolioContrato()
							+ ", Contraseña: " + pass + ". La app le sera enviada a su correo en un momento."); // cuentaAhorroNueva

			EnviaNotificacionesSMS notif = new EnviaNotificacionesSMS();
			notif.enviarNotificacionSMS(sms);
		}
		return folioContrato;
	}

	private Respuesta generaContrato(String cuenta, String idSolicitante) {
		Respuesta resp = new Respuesta();
		boolean registradoServDig = false;

		GeneracionFolioLogic folioLogic = new GeneracionFolioLogic();
		String folio = "";

		// ReporteContratoServiciosElectronicosOBJ datosReporte = new
		// ReporteContratoServiciosElectronicosOBJ();

		RegistroServiciosDigitalesOBJ regServ = rdao.buscarRegistroServiciosDigitales(cuenta);
		RegistroCodiOBJ regCodi = rcdao.buscarRegistroCodi(cuenta);

		if (regServ != null)
			registradoServDig = true;

		if (registradoServDig && regServ.getEstatus() == 3) {
			resp.setCodigo(-1);
			resp.setMensaje("ERROR CONTRATO VIGENTE");

		} else {

			if (registradoServDig && regServ.getFolioContrato() != null && !regServ.getFolioContrato().equals("")
					&& (regServ.getEstatus() == 1 || regServ.getEstatus() == 2))
				folio = regServ.getFolioContrato();
			else
				folio = folioLogic.getFolio();

			if (!registradoServDig) {
				regServ = new RegistroServiciosDigitalesOBJ();
				regServ.setCuenta(cuenta);
				regServ.setEstatus(1);
				regServ.setFolioContrato(folio);

				rdao.guardarRegistroServiciosDigitales(regServ);
			} else {
				regServ.setEstatus(1);
				regServ.setFolioContrato(folio);
				rdao.actualizarRegistroServiciosDigitales(regServ);
			}

			if (regCodi == null) {
				regCodi = new RegistroCodiOBJ();
				regCodi.setCuenta(cuenta);
				regCodi.setEstatus(1);
				regCodi.setFolioContrato(folio);
				regCodi.setIdSolicitante(idSolicitante);

				rcdao.guardarRegistroCodi(regCodi);
			} else {
				regCodi.setEstatus(1);
				regCodi.setFolioContrato(folio);

				rcdao.actualizarRegistroCodi(regCodi);
			}

			resp.setCodigo(0);
			resp.setMensaje("OK");
			GuardarServiciosDigitalesReq req = new GuardarServiciosDigitalesReq();
			req.setCodi(regCodi);
			req.setServicios(regServ);
			resp.setData(gson.toJson(req));

		}
		return resp;
	}

	private void generarPIN(String cuenta, String referenciaCuenta, String nombreSolicitante, String cuentaCorreo,
			long idCanalAtencion) {

		log.info("Enviando correo con PIN");
		String pin = pdao.generarPIN(cuenta);

		// Cifrado del pin
		String saKey1 = "1AC0FAF989B8DADE";
		String saKey2 = "A509DDD18187530A";
		String pinCifrado = cifrarPIN(pin, referenciaCuenta, saKey1, saKey2);

		adao.actualizarNipAhorroContrato(cuenta, pinCifrado);
		String nombreCanal = cdao.consultarCanal(idCanalAtencion);
		audao.registrarAuditoriaCuentaAhorro(cuenta, nombreCanal);

		GeneraReporteTarjetaReq req = new GeneraReporteTarjetaReq();
		req.setMailFrom(REMITENTE_NOTIFICACIONES);
		req.setMailTo(cuentaCorreo);
		req.setSubject(ASUNTO_CORREO_NIP);
		String mailBody = generaCuerpoCorreoPIN(nombreSolicitante, cuenta, pin);
		req.setMailBody(mailBody);

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				GeneraNotificacionesMail generaNotificaciones = new GeneraNotificacionesMail();
				generaNotificaciones.enviarPIN(req);
				log.info("PIN enviado por correo");
			}
		});
	}
	
	private void generarNotificacionCallCenter(String nombreCliente, String numeroCuenta, String numeroSolicitante, String producto) {

		log.info("Enviando correo call center");

		GeneraNotificacionCallCenterReqOBJ req = new GeneraNotificacionCallCenterReqOBJ();
		req.setMailFrom(ConceptosUtil.REMITENTE_EMAIL_CALLCENTER);
		req.setMailTo(ConceptosUtil.DESTINATARIO_EMAIL_CALLCENTER);
		req.setSubject(ConceptosUtil.ASUNTO_EMAIL_CALLCENTER);
		String mailBody = generaBodyCorreoNotificacionCallCenter(nombreCliente, numeroCuenta, numeroSolicitante, producto);
		req.setMailBody(mailBody);

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				GeneraNotificacionesMail generaNotificaciones = new GeneraNotificacionesMail();
				generaNotificaciones.enviarNotificacionCallCenter(req);
				log.info("PIN enviado por correo");
			}
		});
	}

	private String cifrarPIN(String pin, String referencia, String key1, String key2) {
		String securePin = "";
		try {
			iso9564 crypt = new iso9564();
			crypt.setDebugEnable(0, 0);
			crypt.setPanPadChar('0');
			crypt.setPinPadChar('F');
			crypt.setKeyEncripterTralaterKey("1111222233334444", "1111111111111111");

			String referenciaTmp = "9001" + referencia + "01";
			pin = referenciaTmp.substring(4, 15) + "1" + pin;
			securePin = crypt.getPvv(pin, key1, key2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return securePin;
	}

	private String generaCuerpoCorreoPIN(String nombre, String cuenta, String pin) {
		String body = "";
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\">"
				+ "<head>" + "<meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\">"
				+ "<meta name=Generator content=\"Microsoft Word 12 (filtered medium)\">" + "<style>" + "<!--"
				+ " @font-face" + "	{font-family:\"Cambria Math\";" + "	panose-1:2 4 5 3 5 4 6 3 2 4;}" + "@font-face"
				+ "	{font-family:Calibri;" + "	panose-1:2 15 5 2 2 2 4 3 2 4;}" + "@font-face"
				+ "	{font-family:Tahoma;" + "	panose-1:2 11 6 4 3 5 4 4 2 4;}" + "@font-face"
				+ "	{font-family:\"Century Gothic\";" + "	panose-1:2 11 5 2 2 2 2 2 2 4;}" + "@font-face"
				+ "	{font-family:\"Ubuntu Light\";" + "	panose-1:2 11 3 4 3 6 2 3 2 4;}"
				+ " p.MsoNormal, li.MsoNormal, div.MsoNormal" + "	{margin:0cm;" + "	margin-bottom:.0001pt;"
				+ "	font-size:11.0pt;" + "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}"
				+ "a:link, span.MsoHyperlink" + "	{mso-style-priority:99;" + "	color:#0563C1;"
				+ "	text-decoration:underline;}" + "a:visited, span.MsoHyperlinkFollowed" + "	{mso-style-priority:99;"
				+ "	color:#954F72;" + "	text-decoration:underline;}" + "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate"
				+ "	{mso-style-priority:99;" + "	mso-style-link:\"Texto de globo Car\";" + "	margin:0cm;"
				+ "	margin-bottom:.0001pt;" + "	font-size:8.0pt;" + "	font-family:\"Tahoma\",\"sans-serif\";"
				+ "	color:#3B547B;}" + "span.EstiloCorreo17" + "	{mso-style-type:personal-compose;"
				+ "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}" + "span.TextodegloboCar"
				+ "	{mso-style-name:\"Texto de globo Car\";" + "	mso-style-priority:99;"
				+ "	mso-style-link:\"Texto de globo\";" + "	font-family:\"Tahoma\",\"sans-serif\";" + "	color:#3B547B;}"
				+ ".MsoChpDefault" + "	{mso-style-type:export-only;}" + "@page Section1" + "	{size:612.0pt 792.0pt;"
				+ "	margin:70.85pt 3.0cm 70.85pt 3.0cm;}" + "div.Section1" + "	{page:Section1;}" + "-->" + "</style>"
				+ "<!--[if gte mso 9]><xml>" + "<o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" />"
				+ "</xml><![endif]--><!--[if gte mso 9]><xml>" + " <o:shapelayout v:ext=\"edit\">"
				+ "  <o:idmap v:ext=\"edit\" data=\"1\" />" + " </o:shapelayout></xml><![endif]-->" + "</head>"
				+ "<body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\">" + "<div class=Section1>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "<img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br />"
				+ "</span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado(a) <b>"
				+ nombre + "</b>:<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ " Te compartimos el NIP de tu cuenta de ahorro FACIL que acabas de crear con la terminaci&#243;n: ******"
				+ cuenta.substring(cuenta.length() - 4)
				+ ", con &#233;l podr&#225;s consultar su saldo desde tu aplicaci&#243;n Paga Facil. <o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt; text-align:center;'><b><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ pin + "<o:p></o:p></span></b></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "*No debes proporcionar tu NIP.<br>" + "*No compartas tu NIP a desconocidos.<br>"
				+ "*ASP Integra Opciones no realiza llamadas solicitándote proporciones tu NIP.<br>"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><b><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Atentamente<o:p></o:p></span></b></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Opciones Empresariales del Noreste, S.A. de C.V. S.F.P.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "*Para dudas y sugerencias llámanos al 01800 462 7373 o al 612 123 6250." + "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "</div>"
				+ "</body>" + "</html>";
		return body;
	}
	
	public void generaAutorizacion(AhorroContrato ahorroContratoNuevo, String nombreSolicitante) {
		HeaderWS header = new HeaderWS();
		header.setIdEmpresa(ConceptosUtil.ID_EMPRESA);
		header.setUsuarioClave(ConceptosUtil.USR_CLAVE);
		header.setIdUsuario(ConceptosUtil.ID_USR);
		header.setIdSucursal(ConceptosUtil.ID_SUCURSAL);
		header.setIpHost(ConceptosUtil.IP_HOST);
		//log.info((String) session.getAttribute("ip"));
		header.setIdCanalAtencion(ConceptosUtil.ID_CANAL);
		
		AutorizacionesPendientesReq req = new AutorizacionesPendientesReq();
		req.setAplicacion("CERO");
		req.setId_aplicacion(0);
		req.setCanal("SUCURSAL");
		req.setClave_canal("SUCURSAL");
		req.setClave_modulo("SOLICITUD");
		req.setClave_aplicacion("CER_PLA_CER");
		req.setId_canal(1);
		req.setId_persona(ahorroContratoNuevo.getSolicitante());
		req.setId_tipo_operacion(ConceptosUtil.TRX_VERIFICA_DATOS_CUENTA_FACIL);
		req.setModulo("SOLICITUD");
		req.setObservacion("Verificar información capturada por cliente en cuenta de ahorro fácil: " + ahorroContratoNuevo.getCuenta());
		req.setOperacion(ahorroContratoNuevo.getCuenta());
		req.setPersona(nombreSolicitante);
		req.setProducto_id(ConceptosUtil.ID_PRODUCTO_CUENTA_FACIL);
		req.setDesc_producto(ConceptosUtil.PRODUCTO_CUENTA_FACIL);
		req.setDesc_canal("SUCURSAL");
		req.setId_auestatus(0);
		req.setMotivo_rechazo("");
		req.setClave_tipo_opera("AUT_CUENTA_SIMP");
		req.setHeaderWS(header);
		req.setSucursal(ahorroContratoNuevo.getSucursalApertura());
		String respuesta = insertaAutorizacion(req);
		log.info(respuesta); 
	}
	
	public static String insertaAutorizacion(AutorizacionesPendientesReq req) {
		String host = ConstantesUtil.AUTORIZACION_WS.concat("/").concat("agregarAutorizacionesPendientes");
		String auth = Credentials.basic(ConceptosUtil.USR_SERV_AUT, ConceptosUtil.PSW_SERV_AUT);
		Gson gson = new Gson();
		MediaType media = MediaType.parse("application/json; charset=utf-8");
		String resp;
		String body = gson.toJson(req);

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(360, TimeUnit.SECONDS)
				.writeTimeout(360, TimeUnit.SECONDS).readTimeout(360, TimeUnit.SECONDS).build();
		Request request = new Request.Builder().url(host).post(RequestBody.create(media, body))
				.header("Authorization", auth).build();

		try {
			Response response = client.newCall(request).execute();

			Respuesta r = gson.fromJson(response.body().string(), Respuesta.class);
			if (r.getCodigo() == 0) {
				log.info("Se ha enviado al modulo de autorizaciones");
				resp = "Se ha enviado al modulo de autorizaciones";
				
			} else {
				log.error(r.getMensaje());
				resp = r.getMensaje();
			}

		} catch (IOException e) {
			log.error(e.getMessage());
			return e.getMessage();
		}
		return resp;
	}

	/**
	 * @return the productoId
	 */
	public static Integer getProductoId() {
		return productoId;
	}

	/**
	 * @param productoId
	 *            the productoId to set
	 */
	public static void setProductoId(Integer productoId) {
		RegistroCuentaAhorroSimplificadaLogic.productoId = productoId;
	}

	/**
	 * @return the rendimientoId
	 */
	public static Integer getRendimientoId() {
		return rendimientoId;
	}

	/**
	 * @param rendimientoId
	 *            the rendimientoId to set
	 */
	public static void setRendimientoId(Integer rendimientoId) {
		RegistroCuentaAhorroSimplificadaLogic.rendimientoId = rendimientoId;
	}

	/**
	 * @return the usuarioId
	 */
	public static Integer getUsuarioId() {
		return usuarioId;
	}

	/**
	 * @param usuarioId
	 *            the usuarioId to set
	 */
	public static void setUsuarioId(Integer usuarioId) {
		RegistroCuentaAhorroSimplificadaLogic.usuarioId = usuarioId;
	}

	/**
	 * @return the comoEnteroId
	 */
	public static Integer getComoEnteroId() {
		return comoEnteroId;
	}

	/**
	 * @param comoEnteroId
	 *            the comoEnteroId to set
	 */
	public static void setComoEnteroId(Integer comoEnteroId) {
		log.info("####### COMO ENTERO ID :: " + comoEnteroId);
		RegistroCuentaAhorroSimplificadaLogic.comoEnteroId = comoEnteroId;
	}

	/**
	 * @return the acesorId
	 */
	public static Integer getAcesorId() {
		return acesorId;
	}

	/**
	 * @param acesorId
	 *            the acesorId to set
	 */
	public static void setAcesorId(Integer acesorId) {
		RegistroCuentaAhorroSimplificadaLogic.acesorId = acesorId;
	}

	/**
	 * @return the tipoCapitalizarId
	 */
	public static Integer getTipoCapitalizarId() {
		return tipoCapitalizarId;
	}

	/**
	 * @param tipoCapitalizarId
	 *            the tipoCapitalizarId to set
	 */
	public static void setTipoCapitalizarId(Integer tipoCapitalizarId) {
		RegistroCuentaAhorroSimplificadaLogic.tipoCapitalizarId = tipoCapitalizarId;
	}

	/**
	 * @return the idCatTelefono
	 */
	public static Integer getIdCatTelefono() {
		return idCatTelefono;
	}

	/**
	 * @param idCatTelefono
	 *            the idCatTelefono to set
	 */
	public static void setIdCatTelefono(Integer idCatTelefono) {
		RegistroCuentaAhorroSimplificadaLogic.idCatTelefono = idCatTelefono;
	}

	/**
	 * @return the observacionDirTel
	 */
	public static String getObservacionDirTel() {
		return observacionDirTel;
	}

	/**
	 * @param observacionDirTel
	 *            the observacionDirTel to set
	 */
	public static void setObservacionDirTel(String observacionDirTel) {
		RegistroCuentaAhorroSimplificadaLogic.observacionDirTel = observacionDirTel;
	}

}

/*
 * private void ActualizaCatAhorroContratoNuevo(AhorroContrato ahorroContrato) {
 * try { Double gat = adao.calculoGatByCuenta(ahorroContrato.getCuenta());
 * if(gat > 0) { ahorroContrato.setGat(gat);
 * adao.actualizaGatAhorroContrato(gat, ahorroContrato.getCuenta());
 * 
 * } }catch(Exception e) { e.printStackTrace(); } }
 * 
 * private String verificaOSCDummy(String ine) { String respusta = "{\r\n" +
 * "  \"claveElector\": \"GMSLIS84080409H800\",\r\n" +
 * "  \"registro\": \"2003 00\",\r\n" + "  \"vigencia\": \"2024\",\r\n" +
 * "  \"tipo\": \"IFE\",\r\n" + "  \"curp\": \"GOS1840804HDFMLS07\",\r\n" +
 * "  \"colonia\": \"CONJ HAB LAS GARZAS 23079\",\r\n" +
 * "  \"emision\": \"2014\",\r\n" + "  \"subTipo\": \"C\",\r\n" +
 * "  \"codigoValidacion\": \"gd1559257213.79\",\r\n" + "  \"sexo\": \"H\",\r\n"
 * + "  \"segundoApellido\": \"SOLORIO\",\r\n" +
 * "  \"ciudad\": \"LA PAZ , B . C . S .\",\r\n" +
 * "  \"seccion\": \"0230\",\r\n" + "  \"calle\": \"C PERA 187\",\r\n" +
 * "  \"nombres\": \"ISRAEL ANTONIO\",\r\n" + "  \"localidad\": \"0001\",\r\n" +
 * "  \"estado\": \"03\",\r\n" + "  \"primerApellido\": \"GOMEZ\",\r\n" +
 * "  \"municipio\": \"003\",\r\n" + "  \"fechaNacimiento\": \"04/08/1984\"\r\n"
 * + "}"; return respusta; }
 * 
 * private String verificaOSCDummyMerino(String ine) { String respusta = "{\r\n"
 * + "  \"edad\": \"26\",\r\n" +
 * "  \"claveElector\": \"CSMRJR84040103H900\",\r\n" +
 * "  \"registro\": \"2003 01\",\r\n" + "  \"vigencia\": \"2020\",\r\n" +
 * "  \"tipo\": \"IFE\",\r\n" + "  \"curp\": \"CAMJ840401HBSSRR02\",\r\n" +
 * "  \"colonia\": \"COL LOS OLIVOS 23040\",\r\n" +
 * "  \"nombres\": \"JORGE DANIEL\",\r\n" + "  \"subTipo\": \"C\",\r\n" +
 * "  \"sexo\": \"H\",\r\n" + "  \"segundoApellido\": \"MERINO\",\r\n" +
 * "  \"ciudad\": \"LA PAZ , B . C . S .\",\r\n" +
 * "  \"seccion\": \"0174\",\r\n" +
 * "  \"calle\": \"BLVD MANUEL MARQUEZ DE LEON 2570\",\r\n" +
 * "  \"codigoValidacion\": \"gd1560014845.39\",\r\n" +
 * "  \"localidad\": \"0001\",\r\n" + "  \"estado\": \"03\",\r\n" +
 * "  \"primerApellido\": \"CASTRO\",\r\n" + "  \"municipio\": \"003\"\r\n" +
 * "}"; return respusta; }
 * 
 * private String verificaOSC(String ine) { String jsonResp = ""; String HOST =
 * "https://ine.nubarium.com:443/ocr/obtener_datos";
 * log.info("## Host OCR NUBARIUM: " + HOST); final Logger log =
 * LogManager.getLogger(ConsultaSaldoAhorroTest.class);
 * 
 * 
 * String auth = Credentials.basic("aspintegraopciones", "_4rg3tn1Xps4"); Gson
 * gson = new Gson(); MediaType media =
 * MediaType.parse("application/json; charset=utf-8"); IneOcrReqOBJ req = new
 * IneOcrReqOBJ();
 * 
 * try {
 * 
 * req.setId(ine); req.setIdReverso("");
 * 
 * String body = gson.toJson(req); //log.info("## Body OCR NUBARIUM: "
 * + body); OkHttpClient client = new OkHttpClient(); Request request = new
 * Request.Builder().url(HOST).post(RequestBody.create(media, body))
 * .header("Authorization", auth).build();
 * 
 * try { Response response = client.newCall(request).execute(); jsonResp =
 * response.body().string(); log.info("## SYNC CALL OCR NUBARIUM: " +
 * jsonResp); //log.info("## SYNC CALL OCR NUBARIUM: " + jsonResp); } catch
 * (IOException e) { log.error(e.getMessage()); } } catch (Exception e) {
 * e.printStackTrace(); jsonResp = ""; }
 * 
 * return jsonResp; }
 * 
 */
