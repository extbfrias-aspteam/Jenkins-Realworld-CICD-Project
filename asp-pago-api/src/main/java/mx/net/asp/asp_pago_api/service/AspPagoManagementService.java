package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.dto.ServiceResponse;
import mx.net.asp.asp_pago_api.request.DetalleCuentaDestinoReq;
import mx.net.asp.asp_pago_api.request.RegistraFavoritoOBJ;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.utilerias.errores.ErroresGenerales;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoManagement;
import mx.net.asp.asp_pago_api.ws.asp.request.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class AspPagoManagementService {

    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoManagement wsAspPagoManagement;
    @Value("${aes.key.initial}")
    private String initialCipherKey;
    @Value("${ms.management.consultar.cp}")
    private String pathConsultarCp;
    @Value("${ms.management.obtener.saldo.ahorro}")
    private String pathObtenerSaldoAhorroSAV2;
    @Value("${ms.management.consulta.domicilio}")
    private String pathConsultaDomicilio;
    @Value("${ms.management.lista.servicios}")
    private String pathListaServicios;
    @Value("${ms.management.lista.recargas}")
    private String pathListaRecargas;
    @Value("${ms.management.enviar.codigo}")
    private String pathEnviarCodigo;
    @Value("${ms.management.reenviar.codigo.correo}")
    private String pathReenviarCodigoPorCorreo;
    @Value("${ms.management.valida.telefono}")
    private String pathValidaTelefono;
    @Value("${ms.management.obtener.favoritos.cuenta}")
    private String pathObtenerFavoritosCuenta;
    @Value("${ms.management.registra.favorito.V2}")
    private String pathRegistraFavoritoV2;
    @Value("${ms.management.eliminar.favorito}")
    private String pathEliminarFavorito;
    @Value("${ms.management.valida.beneficiario.detalle}")
    private String pathValidaBeneficiarioDetalle;
    @Value("${ms.management.alta.beneficiario.detalle}")
    private String pathAltaBeneficiarioDetalle;
    @Value("${ms.management.valida.curp.nubarium}")
    private String pathValidaCurpNub;
    @Value("${ms.management.registrar.token.huella}")
    private String pathRegistrarTokenHuella;
    @Value("${ms.management.obtener.detalle.cuenta}")
    private String pathObtenerDetalleCuenta;
    @Value("${ms.management.obtener.creditos.cuenta}")
    private String pathObtenerCreditosCuenta;
    @Value("${ms.management.obtener.tarjetas.cuenta}")
    private String pathObtenerTarjetasCuenta;
    @Value("${ms.management.obtener.detalle.cuenta.destino}")
    private String pathObtenerDetalleCuentaDestino;
    @Value("${ms.management.valida.curp.cecoban}")
    private String pathValidaCurpCecoban;
    @Value("${ms.management.genera.edo.mov}")
    private String pathGeneraReporteEstadoMovimientosV2;
    private final Gson gson;
    private String cuentaString = "cuenta";
    private String curpString = "curp";
    private String identificadorString = "identificadorCuenta";

    public AspPagoManagementService(CifradoUtil cifradoUtil,
                                    ErrorHandler errorHandler,
                                    WsAspPagoManagement wsAspPagoManagement,
                                    Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.gson = gson;
        this.wsAspPagoManagement = wsAspPagoManagement;
    }

    public String consultarCP(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("cp", (gson.fromJson(respuestaDTO.getData(), ConsultarCpReq.class).getCp()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathConsultarCp, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerSaldoAhorroSAV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(cuentaString, (gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathObtenerSaldoAhorroSAV2, null, HttpMethod.GET, queryParams);
                if (respuestaDTO.getCodigo() == 0)
                    respuestaDTO.setData(cifradoUtil.encryptResponse(respuestaDTO.getData()));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String consultaDomicilio(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("solicitante_id", (gson.fromJson(respuestaDTO.getData(), SolicitanteReq.class).getSolicitanteId()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathConsultaDomicilio, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String listaServicios() {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = wsAspPagoManagement.enviarPeticion(pathListaServicios, null, HttpMethod.GET, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String listaRecargas() {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = wsAspPagoManagement.enviarPeticion(pathListaRecargas, null, HttpMethod.GET, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String enviarCodigo(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathEnviarCodigo, gson.fromJson(respuestaDTO.getData(),
                        EnviarCodigoReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validaTelefono(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathValidaTelefono, gson.fromJson(respuestaDTO.getData(),
                        ValidaTelefonoReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerFavoritosCuenta(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(cuentaString, (gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathObtenerFavoritosCuenta, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String registraFavoritoV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        RespuestaDTO respuestaDTOAux = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            RegistraFavoritoOBJ registraFavoritoOBJ = gson.fromJson(respuestaDTO.getData(), RegistraFavoritoOBJ.class);
            respuestaDTOAux = cifradoUtil.decodeAndDecryptRequest(registraFavoritoOBJ.getData());
            RegistraFavoritoReq registraFavoritoReq = gson.fromJson(respuestaDTOAux.getData(), RegistraFavoritoReq.class);
            registraFavoritoReq.setGuardarFavorito(registraFavoritoOBJ.isGuardarFavorito());
            registraFavoritoReq.setNumeroCuentaCoDi(registraFavoritoOBJ.getNumeroCuentaCoDi());
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathRegistraFavoritoV2, registraFavoritoReq, HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String eliminarFavorito(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathEliminarFavorito, gson.fromJson(respuestaDTO.getData(),
                        EliminarFavoritoReq.class), HttpMethod.PATCH, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validaBeneficiarioDetalle(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(cuentaString, (gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathValidaBeneficiarioDetalle, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String altaBeneficiarioDetalle(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathAltaBeneficiarioDetalle, gson.fromJson(respuestaDTO.getData(),
                        AltaDetalleBeneficiarioReq.class), HttpMethod.POST, null);
                respuestaDTO.setData(cifradoUtil.encryptResponse(respuestaDTO.getData()));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String registrarTokenHuella(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathRegistrarTokenHuella, gson.fromJson(respuestaDTO.getData(),
                        RegistraTokenHuellaReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerDetalleCuenta(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(cuentaString, (gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathObtenerDetalleCuenta, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerListaCreditos(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(cuentaString, (gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathObtenerCreditosCuenta, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerTarjetas(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(cuentaString, (gson.fromJson(respuestaDTO.getData(), CuentaReq.class).getCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathObtenerTarjetasCuenta, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validaCurpNub(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(curpString, (gson.fromJson(respuestaDTO.getData(), CurpReq.class).getCurp()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathValidaCurpNub, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerDetalleCuentaDestino(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put(identificadorString, (gson.fromJson(respuestaDTO.getData(), DetalleCuentaDestinoReq.class).getIdentificadorCuenta()));
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathObtenerDetalleCuentaDestino, null, HttpMethod.GET, queryParams);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validaCurpCecoban(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathValidaCurpCecoban, gson.fromJson(respuestaDTO.getData(),
                        ValidaCurpCecobanReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String generaReporteEstadoMovimientosV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoManagement.enviarPeticion(pathGeneraReporteEstadoMovimientosV2,
                        gson.fromJson(respuestaDTO.getData(), SolicitudEdoCuentaReq.class),
                        HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public ServiceResponse enviaCodigo2FA(String req) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            respuesta = wsAspPagoManagement.enviarPeticion(pathEnviarCodigo,
                    gson.fromJson(req, EnviarCodigoReq.class),
                    HttpMethod.POST, null);

            return RespuestaUtils.evaluaRespuesta(respuesta, respuesta.getData(), false);
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));

            return new ServiceResponse(gson.toJson(respuesta), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ServiceResponse reenviaCodigo2FAPorCorreo(String req) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            respuesta = wsAspPagoManagement.enviarPeticion(pathReenviarCodigoPorCorreo,
                    gson.fromJson(req, EnviarCodigoReq.class),
                    HttpMethod.POST, null);

            return RespuestaUtils.evaluaRespuesta(respuesta, respuesta.getData(), false);
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));

            return new ServiceResponse(gson.toJson(respuesta), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}