using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Core.Commons.Constants;
using Asp.Api.Azul.Core.Commons.Enums;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Api.Azul.Infrastructure.Services.MonitorPlus;
using Asp.Api.Azul.Infrastructure.Utils;

using Asp.Api.Azul.Kafka.Dtos.PrevencionFraudes;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Dtos.Traspasos;
using Asp.Api.Azul.Kafka.Producer;
using Asp.Api.Azul.Kafka.Topics;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Services.TraspasosCoreService;
using Asp.Api.Azul.Utilities.GestionTokens;
using Asp.Cifrado.Services;
using Confluent.Kafka;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Components.Routing;
using Microsoft.AspNetCore.Http.Extensions;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Npgsql;
using System.ComponentModel.DataAnnotations;
using System.Text.Json;
using System.Text.RegularExpressions;

namespace Asp.Api.Azul.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class PagoController : ControllerBase
    {
        private readonly IHttpContextAccessor _httpContext;
        private readonly ILogsBusiness _logsBusiness;
        private readonly IPagoBusiness _pagoBusiness;
        private readonly IUserResolver _userResolver;
        private readonly IEncriptionService _encriptionService;
        private readonly IConfiguration _configuration;
        private readonly IAspLogservice _aspLogservice;
        private readonly KafkaProducerService _kafkaProducer;
        private readonly IContingencyServices _contingencyServices;
        private readonly IMonitorPlusService _monitorPlusService;
        private readonly IConfigurationService _configurationService;
        private readonly LoggerHelper _logger;
        private readonly ITraspasosCoreService _traspasosCoreService;
        private readonly ResilientExecutor _executor;

        public PagoController(IHttpContextAccessor httpContext, ILogsBusiness logsBusiness, IPagoBusiness pagoBusiness, IUserResolver userResolver, IEncriptionService encriptionService, IConfiguration configuration, IAspLogservice aspLogservice, KafkaProducerService kafkaProducer
            , IContingencyServices contingencyServices, IMonitorPlusService monitorPlusService, IConfigurationService configurationService, LoggerHelper logger, ITraspasosCoreService traspasosCoreService, ResilientExecutor executor)
        {
            _httpContext = httpContext;
            _logsBusiness = logsBusiness;
            _pagoBusiness = pagoBusiness;
            _userResolver = userResolver;
            _encriptionService = encriptionService;
            _configuration = configuration;
            _aspLogservice = aspLogservice;

            _kafkaProducer = kafkaProducer;
            _contingencyServices = contingencyServices;
            _monitorPlusService = monitorPlusService;
            _configurationService = configurationService;
            _logger = logger;
            _traspasosCoreService = traspasosCoreService;
            _executor = executor;

        }

        [HttpPost]
        [Route("/api-test/v1/pago/eyu_pago_cifrado_test")]
        public async Task<ActionResult<string>> TestPagoCifradoFirmado([FromBody] PagoRequest request)
        {

            DateTime currentTime = DateTime.Now;
            string IDENTIFICADOR = "ID_" + currentTime.ToString("yyyyMMddHHmmssffffff");
            _logger.LogInformation("Inicio TestPagoCifradoFirmado");
            var tiempoLlegada = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;
            _logger.LogInformation($"ID del pblu {idPblu}");
            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            string tokenPortal = _httpContext.HttpContext.Request.Headers["token_portal"].ToString();
            string claveRastreo = string.Empty;
            var jsonDescifrado = "null";

            try
            {
                Random random = new Random();
                int randomNumber = random.Next(0, 2); // Genera 0 o 1
                if (randomNumber == 1) throw new Exception("Se lanzó una excepción de prueba.");
                var tokenPortalValidar = _configuration["Portal:Token"];
                var isPortal = !string.IsNullOrEmpty(tokenPortal) && tokenPortal == tokenPortalValidar;
                jsonDescifrado = _encriptionService.Decript(request.Pago, llave, IDENTIFICADOR);
                //print
                _logger.LogInformation($"JSON descifrado->{jsonDescifrado}");
                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu, isPortal, IDENTIFICADOR))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }

                OrdenPagoDto ordenPago = JsonSerializer.Deserialize<OrdenPagoDto>(jsonDescifrado);
                claveRastreo = ordenPago.CveRastreo;

                ConciliacionConClave conciliacionConClave = new ConciliacionConClave()
                {
                    ClaveRastreo = claveRastreo
                };

                var response = JsonSerializer.Serialize(conciliacionConClave);
                _logger.LogInformation("Se cifrara el objeto a enviar");
                var respuestaEncriptada = _encriptionService.Encript(response, idPblu, IDENTIFICADOR);

                Response.Headers.Add("Key", respuestaEncriptada.Key);
                Response.Headers.Add("Signature", respuestaEncriptada.Firma);
                var dataRespuesta = new
                {
                    saldo_actual = respuestaEncriptada.JsonCifrado
                };

                _logger.LogInformation("Fin del metodo TestPagoCifradoFirmado, se responde al participante un exitoso (Ok)");
                return Ok(dataRespuesta);
            }
            catch (Exception ex)
            {
                _logger.LogInformation($"{ex.StackTrace}");
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = claveRastreo,
                    IdError = -1,
                    IdLog = -1,
                    Mensaje = "Este es un error aleatorio de prueba"
                };
                return BadRequest(response);
            }



        }
        [HttpPost]
        [Route("/api/v1/pago/eyu_pago_cifrado_firmado")]
        //public ActionResult<string> PagoCifradoFirmado([FromBody] PagoRequest request)
        public async Task<ActionResult<string>> PagoCifradoFirmado([FromBody] PagoRequest request)
        {

            var IDENTIFICADOR = Guid.NewGuid().ToString();
            _logger.LogInformation($"Inicio eyu_pago_cifrado_firmado con identificador {IDENTIFICADOR}");

            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;
            int cert = user.IdCertificado;

            //print
            _logger.LogInformation($"ID del pblu {idPblu}");
            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            string tokenPortal = _httpContext.HttpContext.Request.Headers["token_portal"].ToString();
            string claveRastreo = string.Empty;
            var jsonDescifrado = "null";
            try
            {
                var tokenPortalValidar = _configuration["Portal:Token"];
                var isPortal = !string.IsNullOrEmpty(tokenPortal) && tokenPortal == tokenPortalValidar;
                jsonDescifrado = _encriptionService.Decript(request.Pago, llave, IDENTIFICADOR);
                //print
                _logger.LogInformation($"JSON descifrado->{jsonDescifrado}");
                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu, isPortal, IDENTIFICADOR))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }


                //var jsonDescifrado = request.Pago;

                OrdenPagoDto ordenPago = JsonSerializer.Deserialize<OrdenPagoDto>(jsonDescifrado);
                claveRastreo = ordenPago.CveRastreo;

                DtoPagoValidado result = null;

                if (int.Parse(ordenPago.BancoDestino) != 90659)
                {
                    var tipoValidacion = await _configurationService.GetWithCache<TipoValidacionAntifraude>(GeneralConstants.MonitorPlus.USE_MONITORPLUS, TimeSpan.FromMinutes(5));
                    //bandera para decidir si escoger monitor plus o prev de fraudes
                    if (tipoValidacion == TipoValidacionAntifraude.Prevencion || tipoValidacion == TipoValidacionAntifraude.Ambos)
                    {
                        //Prevencion de fraudes
                        _logger.LogInformation($"SE UTILIZA EL CANAL DE PREVENCIÓN DE FRAUDES");

                        //Es un pago a tercero
                        _logger.LogInformation($"Inicia el metodo ObtenerDatosCuenta ");
                        var datosCuenta = await _pagoBusiness.ObtenerDatosCuenta(IDENTIFICADOR, ordenPago.Clabe, decimal.Parse(ordenPago.Monto));
                        //_logger.LogInformation($"Finaliza el metodo ObtenerDatosCuenta ");


                        var datosPrevFraudes = new DatosPrevencionFraudeDto
                        {
                            IdPblu = idPblu,
                            DatosCuenta = datosCuenta,
                            DatosOriginales = new DtoDatosOriginalesPago
                            {
                                OrdenPago = ordenPago,
                                JsonPago = jsonDescifrado,
                                Firma = firma,
                                Llave = cert.ToString(),
                                IdPblu = idPblu,
                                IsPortal = isPortal
                            }
                        };
                        await _pagoBusiness.ValidarPago(IDENTIFICADOR, idPblu, isPortal, ordenPago);

                        _logger.LogInformation($"Se publica el evento mediante Kafka: {Topicos.PrevFraude_SolicitudIniciada}");
                        await _kafkaProducer.EnviarMensajeAsync(Topicos.PrevFraude_SolicitudIniciada, datosPrevFraudes);
                    }
                    else
                    {
                        _logger.LogInformation($"SE UTILIZA EL CANAL DE MONITOR PLUS");
                        //Monitor Plus
                        result = await _pagoBusiness.ValidaPago(ordenPago, jsonDescifrado, idPblu, firma, 8, cert, isPortal, IDENTIFICADOR);

                            var eventMessage = new SpeiOutDto
                            {
                                Pago = result.PagoJson,
                                ClaveRastreo = result.CveRastreo,
                                IdPblu = idPblu,
                                Key = string.Empty,
                                Signature = string.Empty,
                                Proveedor = result.Proveedor
                            };

                       
                            var respuestaMonitorPlus = await _monitorPlusService.ValidarOperacionMonitorPlus(ordenPago, idPblu);
                            if (respuestaMonitorPlus.Success)
                            {


                                var isContingency = await _contingencyServices.Validate(idPblu, 1, ordenPago, Topicos.SpeiOutSies, eventMessage);
                                _logger.LogInformation($"isContingency= {isContingency}");
                                if (!isContingency)
                                {
                                _logger.LogInformation($"Entra en flujo de contingencia.");
                                await _kafkaProducer.EnviarMensajeAsync(Topicos.SpeiOutSies, eventMessage);
                                }

                            }
                            else
                            {
                                _logger.LogInformation($"El pago fallo al validarse en Monitor Plus. El flujo termina.");
                                return BadRequest($"Ocurrió un error al procesar el pago con clave {claveRastreo}");
                            }
                        

                       
                    }



                }
                else
                {

                    _logger.LogInformation($"Iniciara el metodo ValidaPago ");
                    result = await _pagoBusiness.ValidaPago(ordenPago, jsonDescifrado, idPblu, firma, 8, cert, isPortal, IDENTIFICADOR);
                    _logger.LogInformation($"Finalizo el metodo ValidaPago ");

                    try
                    {
                        if (result != null)
                        {
                            if (result.toAsp) //Validacion para Eiyu a ASP Kafka
                            {
                                _logger.LogInformation($"Inicia proceso de envio de traspaso EIYU con ASP mediante Kafka {Topicos.TraspasoEiyuToAsp}");
                                Console.WriteLine("Traspasos de Eiyu a ASP" + result.toAsp);
                                var eventMessage = new TraspasoEiyuToAspDto
                                {
                                    ConceptoPago = result.TraspasoAsp.ConceptoPago,
                                    IdTipoPago = result.TraspasoAsp.IdTipoPago,
                                    ClaveRastreo = result.TraspasoAsp.ClaveRastreo,
                                    FechaCaptura = result.TraspasoAsp.FechaCaptura,
                                    CuentaBeneficiario = result.TraspasoAsp.CuentaBeneficiario,
                                    CuentaOrdenante = result.TraspasoAsp.CuentaOrdenante,
                                    Monto = result.TraspasoAsp.Monto,
                                    IdPblu = idPblu,
                                    AbonoJson = result.TraspasoAsp.AbonoJson
                                };

                               
                                await _kafkaProducer.EnviarMensajeAsync(Topicos.TraspasoEiyuToAsp, eventMessage);
                            }
                            else //Validacion para Eiyu a Eiyu
                            {
                                if (result.CloudOrigen != result.CloudDestino)

                                {
                                    _logger.LogInformation("Se enviara la notificaçión del abono al servicio Traspasos Core");


                                    //await _traspasosCoreService.EnviarNotificaciónAbono(result.TraspasosCore);
                                    //var context = new Context();
                                    //context["uuid"] = ;
                                    await _traspasosCoreService.EnviarNotificaciónAbono(result.TraspasosCore);

                                    _logger.LogInformation("Termina el envio de la notificaçión del abono al servicio Traspasos Core");



                                    _logger.LogInformation("Se publica el cambio de estado en kafka");
                                    await _kafkaProducer.EnviarMensajeAsync(Topicos.TopicTraspasoCoreCambioEstadoHandlerKafka, result.CambioEstadoTraspaso);


                                }
                                else
                                {
                                    //es Azure

                                    _logger.LogInformation($"Inicia proceso de envio de traspaso EIYU con cuenta EIYU mediante Kafka {Topicos.TraspasoEiyu}");
                                    await _kafkaProducer.EnviarMensajeAsync(Topicos.TraspasoEiyu, result.AbonoEiyu, true);
                                    _logger.LogInformation($"Traspaso entre cuentas EIYU realizado exitosamente.");
                                    //>>>>>>> origin/rqm1040_kubernetes_v3
                                }

                            }

                        }
                    }
                    catch (Exception e)
                    {
                        _logger.LogInformation($"Error al enviar el evento por Kafka {Topicos.TraspasoEiyu}. {e.Message}");
                        _logger.LogInformation($"Error al enviar el evento por Kafka {Topicos.TraspasoEiyu}. {e.StackTrace}");
                        await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", request, new { Signature = firma, Key = llave }, e, idPblu, claveRastreo);
                        await _pagoBusiness.PagoCambioEstado(claveRastreo, 6, "Error de comunicación con el Motor", DateTime.Now, 0);
                        throw new ErrorComunicacionPSPSEI("Error al procesar el Pago, favor de reintentarlo.");
                    }

                }





                //_logger.LogInformation("Se serializa el objeto");
                ConciliacionConClave conciliacionConClave = new ConciliacionConClave()
                {
                    ClaveRastreo = claveRastreo
                };

                var response = JsonSerializer.Serialize(conciliacionConClave);
                //_logger.LogInformation("Se cifrara el objeto a enviar");
                var respuestaEncriptada = _encriptionService.Encript(response, idPblu, IDENTIFICADOR);

                Response.Headers.Add("Key", respuestaEncriptada.Key);
                Response.Headers.Add("Signature", respuestaEncriptada.Firma);
                var dataRespuesta = new
                {
                    saldo_actual = respuestaEncriptada.JsonCifrado
                };


                //var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                //await _logsBusiness.RegistraTiempos(claveRastreo, idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, dataRespuesta.ToString());
                _logger.LogInformation("Fin PagoCifradoFirmado, response OK pblu");
                return Ok(dataRespuesta);
            }
            catch (ErrorGenerico eg) when (eg is ErrorPagoExistente ||
                                           eg is ErrorFirma ||
                                           eg is ErrorCuentaMalformada ||
                                           eg is ErrorPeticionMalformada ||
                                           eg is ErrorCuentaTDD ||
                                           eg is ErrorClabeSinUDN ||
                                           eg is ErrorUdnNoExiste ||
                                           eg is ErrorConsultaCuenta ||
                                           eg is CertificadoDobleError ||
                                           eg is ErrorClaveRastreoNoExiste ||
                                           eg is ErrorCuentaDestino ||
                                           eg is ErrorBlackList ||
                                           eg is ErrorMontoLimite ||
                                           eg is ErrorPagoSinRefNum)
            {
                _logger.LogInformation($"Ocurrio un error ,{eg.Message}");
                _logger.LogInformation($"Ocurrio un error ,{eg.StackTrace}");
                var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());

                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = eg.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", request, new { Signature = firma, Key = llave }, eg, idPblu, claveRastreo);
                else
                    await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, eg, idPblu, claveRastreo);

                //var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                //await _logsBusiness.RegistraTiempos(claveRastreo, idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, response.Mensaje);
                _logger.LogInformation($"Termina petición: eyu_pago_cifrado_firmado, se responde al participante un BadRequest");
                return BadRequest(response);
            }
            catch (ErrorGenerico eg) when (eg is ErrorSaldoInsuficiente)
            {
                _logger.LogInformation($"Ocurrio un error ,{eg.Message}");
                _logger.LogInformation($"Ocurrio un error ,{eg.StackTrace}");
                var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());

                //	$"Se ha rechazado un pago por saldo insuficiente en su cuenta en Blu. El sistema Blu ha detectado que no cuenta con saldo suficiente para realizar el pago recibido. Identificador de log: {logError.IdLog}. Identificador de Error: {eg.IdError}. Por favor, realize un movimiento de abono a su cuenta Blu para continuar realizar su pago.";
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = eg.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", request, new { Signature = firma, Key = llave }, eg, idPblu, claveRastreo);

                else
                    await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, eg, idPblu, claveRastreo);

                // var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                //await _logsBusiness.RegistraTiempos(claveRastreo, idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, response.Mensaje);
                _logger.LogInformation($"Termina petición: eyu_pago_cifrado_firmado, se responde al participante un BadRequest");
                return BadRequest(response);
            }
            catch (Exception e)
            {
                _logger.LogInformation($"Ocurrio un error ,{e.Message}");
                _logger.LogInformation($"Ocurrio un error ,{e.StackTrace}");
                var logError = await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", request, new { Signature = firma, Key = llave }, e, idPblu, claveRastreo);

                else
                    await _aspLogservice.RegistraError("/api/v1/pago/eyu_pago_cifrado_firmado", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, e, idPblu, claveRastreo);

                //var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                //await _logsBusiness.RegistraTiempos(claveRastreo, idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, response.Mensaje);
                _logger.LogInformation($"Termina petición: eyu_pago_cifrado_firmado, se responde al participante un BadRequest");
                return BadRequest(response);
            }
        }


    }
}