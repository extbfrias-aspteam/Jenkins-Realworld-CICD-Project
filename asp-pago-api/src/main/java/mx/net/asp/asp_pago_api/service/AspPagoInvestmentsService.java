package mx.net.asp.asp_pago_api.service;

import java.util.HashMap;
import java.util.Map;

import mx.net.asp.asp_pago_api.ws.asp.request.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoInvestments;

@Slf4j
@Service
public class AspPagoInvestmentsService {

	@Value("${timeout.extended}")
	private Integer timeoutExtended;
	@Value("${ms.investments.obtener.modalidades}")
	private String pathObtenerModalidades;
	@Value("${ms.investments.obtener.plazos.porcentajes}")
	private String pathObtenerPlazosPorcentajes;
	@Value("${ms.investments.obtener.tipos.reinversiones}")
	private String pathObtenerTiposReinversiones;
	@Value("${ms.investments.obtener.inversiones}")
	private String pathObtenerInversiones;
	@Value("${ms.investments.obtener.detalle.inversion}")
	private String pathObtenerDetalleInversion;
	@Value("${ms.investments.alta.cuenta.inversion}")
	private String pathAltaCuentaInversion;
	@Value("${ms.investments.obtener.movimientos.inversion}")
	private String pathObtenerMovimientosInversion;
	@Value("${ms.investments.alta.reinversion}")
	private String pathAltaReinversion;
	@Value("${ms.investments.cancelar.reinversion}")
	private String pathCancelarReinversion;
	@Value("${ms.investments.simular.inversion}")
	private String pathSimularInversion;
	@Value("${ms.investments.enviar.estado.cuenta}")
	private String pathEnviarEstadoCuenta;
	private final CifradoUtil cifradoUtil;
	private final ErrorHandler errorHandler;
	private final WsAspPagoInvestments aspPagoInvestments;
	private final TimeoutConfigService timeoutConfigService;
	private final Gson gson;

	public AspPagoInvestmentsService(CifradoUtil cifradoUtil, ErrorHandler errorHandler,
			WsAspPagoInvestments aspPagoInvestments, TimeoutConfigService timeoutConfigService, Gson gson) {
		super();
		this.cifradoUtil = cifradoUtil;
		this.errorHandler = errorHandler;
		this.aspPagoInvestments = aspPagoInvestments;
		this.timeoutConfigService = timeoutConfigService;
		this.gson = gson;
	}

	public String obtenerModalidades() {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		try {
			respuestaDTO = aspPagoInvestments.enviarPeticion(pathObtenerModalidades, null, HttpMethod.GET, null);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String obtenerPlazosPorcentajes(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		try {
			respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
			if (respuestaDTO.getCodigo() == 0) {
				Map<String, String> queryParams = new HashMap<>();
				queryParams.put("modalidad",
						(gson.fromJson(respuestaDTO.getData(), ModalidadReq.class).getTipoModalidadId()));
				respuestaDTO = aspPagoInvestments.enviarPeticion(pathObtenerPlazosPorcentajes, null, HttpMethod.GET,
						queryParams);
			}
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String obtenerTiposReinversiones(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		try {
			respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
			if (respuestaDTO.getCodigo() == 0) {
				Map<String, String> queryParams = new HashMap<>();
				queryParams.put("modalidad",
						(gson.fromJson(respuestaDTO.getData(), ModalidadReq.class).getTipoModalidadId()));
				respuestaDTO = aspPagoInvestments.enviarPeticion(pathObtenerTiposReinversiones, null, HttpMethod.GET,
						queryParams);
			}
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String obtenerInversiones(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		try {
			respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
			if (respuestaDTO.getCodigo() == 0) {
				Map<String, String> queryParams = new HashMap<>();
				queryParams.put("cuentaPadre",
						(gson.fromJson(respuestaDTO.getData(), CuentaPadreReq.class).getCuentaPadre()));
				respuestaDTO = aspPagoInvestments.enviarPeticion(pathObtenerInversiones, null, HttpMethod.GET,
						queryParams);
			}
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String obtenerDetalleInversion(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		try {
			respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
			if (respuestaDTO.getCodigo() == 0) {
				Map<String, String> queryParams = new HashMap<>();
				queryParams.put("cuentaInversion",
						(gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
				respuestaDTO = aspPagoInvestments.enviarPeticion(pathObtenerDetalleInversion, null, HttpMethod.GET,
						queryParams);
			}
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String altaCuentaInversion(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = aspPagoInvestments.enviarPeticion(pathAltaCuentaInversion, gson.fromJson(respuestaDTO.getData(),
                		AltaCuentaInversionReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);

	}

	public String obtenerMovimientosInversion(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		try {
			respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
			if (respuestaDTO.getCodigo() == 0) {
				Map<String, String> queryParams = new HashMap<>();
				MovimientosInversionReq movimientosInversionReq = gson.fromJson(respuestaDTO.getData(), MovimientosInversionReq.class);
				queryParams.put("cuentaInversion", movimientosInversionReq.getCuenta());
				if (movimientosInversionReq.getLimiteMovs() != null && movimientosInversionReq.getLimiteMovs() > 0) {
					queryParams.put("limiteMovs", String.valueOf(movimientosInversionReq.getLimiteMovs()));
				}
				respuestaDTO = aspPagoInvestments.enviarPeticion(pathObtenerMovimientosInversion, null, HttpMethod.GET,
						queryParams);
			}
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String altaReinversion(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = aspPagoInvestments.enviarPeticion(pathAltaReinversion, gson.fromJson(respuestaDTO.getData(),
                		AltaReinversionReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String cancelarReinversion(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = aspPagoInvestments.enviarPeticion(pathCancelarReinversion, gson.fromJson(respuestaDTO.getData(),
                		CancelarReinversionReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
	}

	public String simularInversion(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = aspPagoInvestments.enviarPeticion(pathSimularInversion, gson.fromJson(respuestaDTO.getData(),
                		SimulaInversionReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
	}
	
	public String enviarEstadoCuenta(String request) {
		RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = aspPagoInvestments.enviarPeticion(pathEnviarEstadoCuenta, gson.fromJson(respuestaDTO.getData(),
                		EstadoCuentaRequest.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
	}



}
