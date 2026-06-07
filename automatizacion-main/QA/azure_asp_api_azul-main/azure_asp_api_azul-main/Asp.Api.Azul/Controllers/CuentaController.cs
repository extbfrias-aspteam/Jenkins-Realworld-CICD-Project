using Asp.Api.Azul.Business.Cuenta;
using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Utilities.GestionTokens;
using Asp.Cifrado.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.Extensions;
using Microsoft.AspNetCore.Mvc;
using Org.BouncyCastle.Asn1.Ocsp;
using Polly;
using System.Text.Json;
using System.Text.Json.Serialization;
using static System.Net.Mime.MediaTypeNames;

namespace Asp.Api.Azul.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CuentaController : ControllerBase
    {
        private readonly IUserResolver _userResolver;
        private readonly IHttpContextAccessor _httpContext;
        private readonly IEncriptionService _encriptionService;
        private readonly ICuentaBusiness _cuentaBusiness;
        private readonly ILogsBusiness _logsBusiness;
        private readonly IAspLogservice _aspLogservice;
        private readonly IValidarDomicilioService _validarDomicilioService;

        public CuentaController(IUserResolver userResolver, IHttpContextAccessor httpContext,
            IEncriptionService encriptionService, ICuentaBusiness cuentaBusiness,
            ILogsBusiness logsBusiness, IAspLogservice aspLogservice, IValidarDomicilioService validarDomicilioService)
        {
            _userResolver = userResolver;
            _httpContext = httpContext;
            _encriptionService = encriptionService;
            _cuentaBusiness = cuentaBusiness;
            _logsBusiness = logsBusiness;
            _aspLogservice = aspLogservice;
            _validarDomicilioService = validarDomicilioService;
        }

        [HttpPost]
        [Route("/api/v1/cuentas/eyu_agregar_cuenta_simplificada")]
        [ApiExplorerSettings(IgnoreApi = true)]
        public DtoClabe AddCuentaSimplificada([FromBody] CuentaRequest request)
        {
            return new DtoClabe();
        }
        [HttpPost]
        [Route("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm")]
        public async Task<ActionResult<DtoClabe>> AddCuentaExpedientePm([FromBody] CuentaRequest request)
        {
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.fff");
            string timestamp = currentTime.ToString("yyyyMMddHHmmssfff");
            Console.WriteLine($"{current_time_formatt} INFO [AddCuentaExpedientePm - {timestamp}] -> Llega al controller");

            var tiempoLlegada = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;
            int cert = user.IdCertificado;
            Console.WriteLine($"{current_time_formatt} INFO [AddCuentaExpedientePm - {timestamp}] -> id pblu= {idPblu}");
            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            var jsonDescifrado="null";
            try
            {
                jsonDescifrado = _encriptionService.Decript(request.Cuenta, llave);


                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }

                DtoCtaExpedientePMoral expedientePMoral =
                    JsonSerializer.Deserialize<DtoCtaExpedientePMoral>(jsonDescifrado);


                if (expedientePMoral.PersonaMoral.Domicilio != null)
                    _validarDomicilioService.ValidarDomicilio(expedientePMoral.PersonaMoral.Domicilio, 'P');
                if (expedientePMoral.RepresentanteLegal.Domicilio != null)
                    _validarDomicilioService.ValidarDomicilio(expedientePMoral.RepresentanteLegal.Domicilio, 'R');



                var result = await _cuentaBusiness.CrearCuentaPersonaMoral(expedientePMoral, idPblu, user.Username, timestamp);
                //_aspLogservice.RegistraCuentaLog("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", "POST", request, "Accepted", Accepted(result), new { Signature = firma, Key = llave }, new { }, idPblu);
                var respuesta = JsonSerializer.Serialize(result);
                var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                await _logsBusiness.RegistraTiempos("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, respuesta);
                Console.WriteLine($"{current_time_formatt} INFO [AddCuentaExpedientePm - {timestamp}] -> Termina el controller, id pblu= {idPblu}");
                return Accepted(result);
            }
            catch (ErrorGenerico eg) when (eg is ErrorPeticionMalformada ||
                                           eg is ErrorUdnNoExiste ||
                                           eg is ErrorFirma ||
                                           eg is ErrorComunicacionPSPSEI ||
                                           eg is ErrorDomicilio)
            {
                var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error,
                    _httpContext.HttpContext.Request.GetDisplayUrl());

                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = eg.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", "POST", request, new { Signature = firma, Key = llave }, eg, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, eg, idPblu);
                
                var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                await _logsBusiness.RegistraTiempos("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, eg.Message);
                Console.WriteLine($"{current_time_formatt} INFO [AddCuentaExpedientePm - {timestamp}] -> Termina el controller, id pblu={idPblu}");
                return BadRequest(response);
            }
            catch (Exception e)
            {
                var logError =await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
                if (jsonDescifrado == "null")
                   await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", "POST",request, new { Signature = firma, Key = llave },e,idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", "POST",jsonDescifrado, new { Signature = firma, Key = llave },e,idPblu);

                var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                await _logsBusiness.RegistraTiempos("/api/v1/cuentas/eyu_agregar_cuenta_expediente_pm", idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, e.Message);
                Console.WriteLine($"{current_time_formatt} INFO [AddCuentaExpedientePm - {timestamp}] -> Termina el controller, id pblu={idPblu}");
                return BadRequest(response);
            }
        }
        [HttpPost]
        [Route("/api/v1/cuentas/eyu_agregar_cuenta_expediente")]
        public async Task<ActionResult<string>> AddCuentaExpediente([FromBody] CuentaRequest request)
        {
            var tiempoLlegada = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;
            int cert = user.IdCertificado;

            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            var jsonDescifrado="null";
            try
            {
                jsonDescifrado = _encriptionService.Decript(request.Cuenta, llave);

                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }

                DtoCtaExpediente expediente = JsonSerializer.Deserialize<DtoCtaExpediente>(jsonDescifrado);

                if (expediente.domicilio != null)
                    _validarDomicilioService.ValidarDomicilio(expediente.domicilio, 'P');


                var result = await _cuentaBusiness.CrearCuentaPersonaFisica(expediente, idPblu, user.Username);

                //_aspLogservice.RegistraCuentaLog("/api/v1/cuentas/eyu_agregar_cuenta_expediente", "POST", request, "Accepted", Accepted(result), new { Signature = firma, Key = llave }, new { }, idPblu);
                //return Ok(result.Clabe);
                var respuesta = JsonSerializer.Serialize(result);
                var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                await _logsBusiness.RegistraTiempos("/api/v1/cuentas/eyu_agregar_cuenta_expediente", idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, respuesta);
                return Accepted(result);
            }
            catch (ErrorGenerico eg) when (eg is ErrorPeticionMalformada ||
                                           eg is ErrorUdnNoExiste ||
                                           eg is ErrorFirma ||
                                           eg is ErrorComunicacionPSPSEI ||
                                           eg is ErrorDomicilio ||
                                           eg is ErrorUdnNoAsociada)
            {
                var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error,
                    _httpContext.HttpContext.Request.GetDisplayUrl());

                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = eg.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente", "POST",request, new { Signature = firma, Key = llave }, eg, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente", "POST",jsonDescifrado, new { Signature = firma, Key = llave }, eg, idPblu);
                
                var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                await _logsBusiness.RegistraTiempos("/api/v1/cuentas/eyu_agregar_cuenta_expediente", idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, eg.Message);
                return BadRequest(response);
            }
            catch (Exception e)
            {
                var logError = await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente", "POST", request, new { Signature = firma, Key = llave }, e, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_agregar_cuenta_expediente", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, e, idPblu);
               
                var tiempoRespuesta = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                await _logsBusiness.RegistraTiempos("/api/v1/cuentas/eyu_agregar_cuenta_expediente", idPblu.ToString(), jsonDescifrado, tiempoLlegada, tiempoRespuesta, e.Message);
                return BadRequest(response);
            }
        }

        [HttpPost]
        [Route("/api/v1/cuentas/eyu_actualiza_cuenta_expediente")]
        public async Task<ActionResult<string>> UpdateCuentaExpediente([FromBody] CuentaRequest request)
        {
            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;
            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            var jsonDescifrado="null";
            try
            {
                jsonDescifrado = _encriptionService.Decript(request.Cuenta, llave);

                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }

                DtoCtaExpedienteActualiza? expediente = JsonSerializer.Deserialize<DtoCtaExpedienteActualiza>(jsonDescifrado);

                if (expediente.domicilio != null)
                    _validarDomicilioService.ValidarDomicilio(expediente.domicilio, 'P');

                var result = await _cuentaBusiness.ActualizaCuentaExpediente(expediente, idPblu, user.Username);

                //_aspLogservice.RegistraCuentaLog("/api/v1/cuentas/eyu_actualiza_cuenta_expediente", "POST", request, "Accepted", Accepted(result), new { Signature = firma, Key = llave }, new { }, idPblu);
                //return Ok(result.Clabe);
                return Accepted(result);
            }
            catch (ErrorGenerico eg) when (eg is ErrorPeticionMalformada ||
                                           eg is ErrorUdnNoExiste ||
                                           eg is ErrorFirma ||
                                           eg is ErrorComunicacionPSPSEI ||
                                           eg is ErrorDomicilio ||
                                           eg is ErrorUdnNoAsociada)
            {
                var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error,
                    _httpContext.HttpContext.Request.GetDisplayUrl());

                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = eg.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente", "POST",request, new { Signature = firma, Key = llave }, eg, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente", "POST",jsonDescifrado, new { Signature = firma, Key = llave }, eg, idPblu);
                return BadRequest(response);
            }
            catch (Exception e)
            {
                var logError = await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
                if(jsonDescifrado=="null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente", "POST", request, new { Signature = firma, Key = llave }, e, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, e, idPblu);

                return BadRequest(response);
            }
        }

        [HttpPost]
        [Route("/api/v1/cuentas/eyu_actualiza_cuenta_expediente_pm")]
        public async Task<ActionResult<string>> UpdateCuentaExpedientePm([FromBody] CuentaRequest request)
        {
            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;
            int cert = user.IdCertificado;

            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            var jsonDescifrado="null";
            try
            {
                jsonDescifrado = _encriptionService.Decript(request.Cuenta, llave);
                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }

                DtoCtaExpedienteActualizaPMoral expedientePMoral =
                    JsonSerializer.Deserialize<DtoCtaExpedienteActualizaPMoral>(jsonDescifrado);

                if (expedientePMoral.PersonaMoral.Domicilio != null)
                    _validarDomicilioService.ValidarDomicilio(expedientePMoral.PersonaMoral.Domicilio, 'P');
                if (expedientePMoral.RepresentanteLegal.Domicilio != null)
                    _validarDomicilioService.ValidarDomicilio(expedientePMoral.RepresentanteLegal.Domicilio, 'R');


                var result = await _cuentaBusiness.ActualizaCuentaExpedienteMoral(expedientePMoral, idPblu, user.Username);

                //_aspLogservice.RegistraCuentaLog("/api/v1/cuentas/eyu_actualiza_cuenta_expediente_pm", "POST", request, "Accepted", Accepted(result), new { Signature = firma, Key = llave }, new { },idPblu);
                return Accepted(result);
            }
            catch (ErrorGenerico eg) when (eg is ErrorPeticionMalformada ||
                                           eg is ErrorUdnNoExiste ||
                                           eg is ErrorFirma ||
                                           eg is ErrorComunicacionPSPSEI ||
                                           eg is ErrorDomicilio ||
                                           eg is ErrorUdnNoAsociada)
            {
                var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error,
                    _httpContext.HttpContext.Request.GetDisplayUrl());

                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = eg.Message
                };
                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente_pm", "POST",request, new { Signature = firma, Key = llave }, eg, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente_pm", "POST",jsonDescifrado, new { Signature = firma, Key = llave }, eg, idPblu);
                return BadRequest(response);
            }
            catch (Exception e)
            {
                var logError = await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
                if(jsonDescifrado== "null")
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente_pm", "POST", request, new { Signature = firma, Key = llave }, e, idPblu);
                else
                    await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_actualiza_cuenta_expediente_pm", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, e, idPblu);
                return BadRequest(response);
            }
        }

        [HttpPost]
        [Route("/api/v1/cuentas/activarCuenta")]
        [Authorize] // Este endpoint requerirá un token válido
        public async Task<ActionResult<ResponseDto>> ActivarCuentaExpediente([FromBody] ActivarCuentaRequest request)
        {
            try
            {
                var user = _userResolver.GetUser();
                int idPblu = user.IdPblue;
                // Validación del token
                var token = HttpContext.Request.Headers["Authorization"].FirstOrDefault()?.Split(" ").Last();
                if (string.IsNullOrWhiteSpace(token))
                {
                    return Unauthorized(new ResponseDto
                    {
                        Codigo = 401,
                        Mensaje = "No se proporcionó el token de autenticación. Por favor, incluya un token válido en el encabezado de la solicitud.",
                        Data = ""
                    });
                }

                // Validación de la longitud de la CLABE
                if (string.IsNullOrWhiteSpace(request?.Clabe) || request.Clabe.Length != 18)
                {
                    return BadRequest(new ResponseDto
                    {
                        Codigo = 1,
                        Mensaje = "La CLABE debe tener exactamente 18 caracteres.",
                        Data = ""
                    });
                }

                // Validar si la cuenta existe
                if (!await _cuentaBusiness.ExisteActivateCuenta(request.Clabe))
                {
                    return NotFound(new ResponseDto
                    {
                        Codigo = 1,
                        Mensaje = "La cuenta no fue encontrada.",
                        Data = ""
                    });
                }

                // Actualizar el campo activo a true
                bool updateResult = await _cuentaBusiness.ActivateCuenta(request.Clabe, idPblu);
                if (updateResult)
                {
                    return Ok(new ResponseDto
                    {
                        Codigo = 0,
                        Mensaje = "Se activó correctamente la cuenta.",
                        Data = ""
                    });
                }
                else
                {
                    return StatusCode(500, new ResponseDto
                    {
                        Codigo = 1,
                        Mensaje = "Ocurrió un error al actualizar la cuenta o ya se encuentra activa",
                        Data = ""
                    });
                }
            }
            catch(Exception ex)
            {
                return StatusCode(500, new ResponseDto
                {
                    Codigo = 1,
                    Mensaje = ex.Message,
                    Data = ""
                });
            }
          
        }
    }
}