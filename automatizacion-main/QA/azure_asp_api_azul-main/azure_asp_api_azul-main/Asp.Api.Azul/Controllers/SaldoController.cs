using System.Globalization;
using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Business.Saldo;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Models.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http.Extensions;
using Microsoft.AspNetCore.Authorization;
using Asp.Api.Azul.Utilities.GestionTokens;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Business.Cuenta;

namespace Asp.Api.Azul.Controllers
{
	[Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class SaldoController : ControllerBase
    {
	    private readonly ISaldoBusiness _saldoBusiness;
	    private readonly IHttpContextAccessor _httpContext;
	    private readonly ILogsBusiness _logsBusiness;
		private readonly IUserResolver _userResolver;
        private readonly IAspLogservice _aspLogservice;
        private readonly ICuentaBusiness _cuentaBusiness;

        public SaldoController(ISaldoBusiness saldoBusiness, IHttpContextAccessor httpContextAccessor, ILogsBusiness logsBusiness, IUserResolver userResolver, IAspLogservice aspLogservice, ICuentaBusiness cuentaBusiness)
	    {
		    _saldoBusiness = saldoBusiness;
			_httpContext = httpContextAccessor;
			_logsBusiness = logsBusiness;
			_userResolver = userResolver;
            _aspLogservice = aspLogservice;
            _cuentaBusiness = cuentaBusiness;
        }

        [HttpPost]
        [AllowAnonymous]
        [Route("/api/v1/saldo/eyu_saldo_udn_es")]
        public async Task<IActionResult> Saldo_udn_es(string udn)
        {
            DateTime currentTime = DateTime.Now;
            string IDENTIFICADOR = "ID_" + currentTime.ToString("yyyyMMddHHmmssffffff");
            GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", "Inicio Saldo_udn_es");

            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;

            GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Consulta el pblu: {idPblu}");

            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();

            try
            {
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Inicia consulta VerificarUdnByPblu");
                bool exist = await _saldoBusiness.VerificarUdnByPblu(idPblu, int.Parse(udn));
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Finaliza consulta VerificarUdnByPblu");
                if (!exist)
                {
                    GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"No existe la udn: {udn} para el participante: {idPblu}. Se retorna saldo_udn=0");
                    return Ok(new { saldo_udn = 0 });
                }
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"SI existe la udn: {udn} para el participante: {idPblu}.");
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Inicia consulta GetSaldoUdnByUdnEs");
                var saldo = await _saldoBusiness.GetSaldoUdnByUdnEs(udn);
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Finaliza consulta GetSaldoUdnByUdnEs");
                var respon = new
                {
                    saldo_udn = saldo
                };
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Finaliza el Flujo, se responde con un estatus 200.");
                return Ok(respon);
            }
            catch (Exception e)
            {
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Error: {e.Message}");
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn_es", $"Error: {e.StackTrace}");
                var logError = await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
               await _aspLogservice.RegistraError("/api/v1/saldo/eyu_saldo_udn_es", "POST", new { }, new { Signature = firma, Key = llave }, e, idPblu);

                return BadRequest(response);
            }
        }



        [HttpPost]
        [Route("/api/v1/saldo/eyu_saldo_udn")]
        public async Task<IActionResult> Saldo_udn(string clabe)
        {
            DateTime currentTime = DateTime.Now;
            string IDENTIFICADOR = "ID_" + currentTime.ToString("yyyyMMddHHmmssffffff");
            GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", "Inicio Saldo_udn");

            var user = _userResolver.GetUser();
            int idPblu = user.IdPblue;

            GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Consulta el pblu: {idPblu}");
            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
        
            try
            {
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Inicia VerificaClabeByIdPblu");
                bool exist = await _cuentaBusiness.VerificaClabeByIdPblu(idPblu, clabe);
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Finaliza VerificaClabeByIdPblu");
                if (!exist)
                {
                    GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"No existe la clabe: {clabe} para el participante: {idPblu}. Se retorna saldo_udn=0");
                    return Ok(new { saldo_udn = 0 });
                }
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Si existe la clabe: {clabe} para el participante: {idPblu}.");
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Inicia GetSaldoUdnByClabe");
                var saldo = await _saldoBusiness.GetSaldoUdnByClabe(clabe);
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Finaliza GetSaldoUdnByClabe");
                var respon = new
                {
                    saldo_udn = saldo
                };
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Finaliza el Flujo, se responde con un estatus 200.");
                return Ok(respon);
            }
            catch (Exception e)
            {
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Error: {e.Message}");
                GenerarLog(IDENTIFICADOR, "eyu_saldo_udn", $"Error: {e.StackTrace}");
                var logError = await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
                ErrorResponse response = new ErrorResponse()
                {
                    CveRastreo = logError.CveRastreo,
                    IdError = logError.IdError,
                    IdLog = logError.IdLog,
                    Mensaje = e.Message
                };
                await _aspLogservice.RegistraError("/api/v1/saldo/eyu_saldo", "POST", new { }, new { Signature = firma, Key = llave }, e, idPblu);

                return BadRequest(response);
            }
        }   
        [HttpGet]
        [Route("/api/v1/saldo/eyu_saldo_dia_actual")]
        public async Task<IActionResult> SaldoDiaActual()
        {
			var user = _userResolver.GetUser();
	        int idPblu = user.IdPblue;
	        int cert = user.IdCertificado;

	        string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
	        string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
			try
			{
				//TODO: tenemos que ver como se llenara la tabla historial_saldo_pblu, en java la llenan con los apis inicio_operacion y cierre_operacion
				var response = await _saldoBusiness.GetSaldoDiaActual(idPblu);
				//_aspLogservice.RegistraSaldoLog("/api/v1/saldo/eyu_saldo_dia_actual", "GET", new { }, "Accepted", Accepted(response), new { Signature = firma, Key = llave }, new { }, idPblu);
				return Accepted(response);
			}
	        catch (ErrorEnProcesoDeConciliacion eg)
	        {
				var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());

				ErrorResponse response = new ErrorResponse()
				{
					CveRastreo = logError.CveRastreo,
					IdError = logError.IdError,
					IdLog = logError.IdLog,
					Mensaje = eg.Message
				};
				
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
				await _aspLogservice.RegistraError("/api/v1/saldo/eyu_saldo_dia_actual", "GET", new { }, new { Signature = firma, Key = llave }, e, idPblu);
				return BadRequest(response);
			}
		}

        [HttpPost]
        [Route("/api/v1/saldo/eyu_saldo")]
        public async Task<IActionResult> Saldo(string clabe)
        {
			var user = _userResolver.GetUser();
			int idPblu = user.IdPblue;
			int cert = user.IdCertificado;

			string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
			string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();

			try
			{
				var saldo = await _saldoBusiness.GetSaldoByClabe(idPblu, clabe);
                //_aspLogservice.RegistraSaldoLog("/api/v1/saldo/eyu_saldo", "POST", new { }, "Accepted", Accepted(saldo), new { Signature = firma, Key = llave }, new { }, idPblu);
                return Accepted(saldo);
			}
			catch (ErrorGenerico eg) when (eg is ErrorUdnNoExiste ||
			                               eg is ErrorUdnNoAsociada)
			{
				var logError = await _logsBusiness.RegistraErrorAzul(eg, idPblu, LogLevel.Error, _httpContext.HttpContext.Request.GetDisplayUrl());
				ErrorResponse response = new ErrorResponse()
				{
					CveRastreo = logError.CveRastreo,
					IdError = logError.IdError,
					IdLog = logError.IdLog,
					Mensaje = eg.Message
				};

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
                await _aspLogservice.RegistraError("/api/v1/saldo/eyu_saldo", "POST", new { }, new { Signature = firma, Key = llave }, e, idPblu);

                return BadRequest(response);
			}
        }

        [HttpGet]
        [Route("/api/v1/saldo/eyu_corte_dia")]
        [ApiExplorerSettings(IgnoreApi = true)]
		[AllowAnonymous]
        public async Task<IActionResult> CorteDia(string llave)
        {
	        if (llave != "3mVFuFFHGvVr1CvRDqYLA8HwB3nVXUk4")
	        {
		        return BadRequest();
	        }

			await _saldoBusiness.CierreDia();
			await Task.Delay(1000);
			await _saldoBusiness.InicioDia();
			return Ok();
        }

        [HttpGet]
        [Route("/api/v1/saldo/eyu_corte_dia_anterior")]
		[ApiExplorerSettings(IgnoreApi = true)]
		[AllowAnonymous]
        public async Task<IActionResult> CorteDiaAnterior(string llave)
        {
            if (llave != "3mVFuFFHGvVr1CvRDqYLA8HwB3nVXUk4")
            {
                return BadRequest();
            }

            await _saldoBusiness.CierreDia(anterior: true);
            await Task.Delay(1000);
            await _saldoBusiness.InicioDia();
            return Ok();
        }


        [HttpGet]
        [Route("/api/v1/saldo/eyu_cierre_dia_anterior")]
		[ApiExplorerSettings(IgnoreApi = true)]
		[AllowAnonymous]
        public async Task<IActionResult> CierreDiaAnterior(string llave)
        {
            if (llave != "3mVFuFFHGvVr1CvRDqYLA8HwB3nVXUk4")
            {
                return BadRequest();
            }

            await _saldoBusiness.CierreDia(anterior:true);
            return Ok();
        }


        [HttpGet]
        [Route("/api/v1/saldo/eyu_inicio_dia")]
		[ApiExplorerSettings(IgnoreApi = true)]
		[AllowAnonymous]
        public async Task<IActionResult> InicioDia(string llave)
        {
            if (llave != "3mVFuFFHGvVr1CvRDqYLA8HwB3nVXUk4")
            {
                return BadRequest();
            }

            await _saldoBusiness.InicioDia();
            return Ok();
        }



        [HttpGet]
        [Route("/api/v1/saldo/eyu_cierre_fecha")]
		[ApiExplorerSettings(IgnoreApi = true)]
		[AllowAnonymous]
        public async Task<IActionResult> CierreFecha(string llave, string fecha)
        {
            if (llave != "3mVFuFFHGvVr1CvRDqYLA8HwB3nVXUk4")
            {
                return BadRequest();
            }
            var date = DateTime.ParseExact(fecha, "yyyy-MM-dd", CultureInfo.InvariantCulture);
            await _saldoBusiness.CierreDia(date);
            return Ok();
        }


        [HttpGet]
        [Route("/api/v1/saldo/eyu_inicio_fecha")]
		[ApiExplorerSettings(IgnoreApi = true)]
		[AllowAnonymous]
        public async Task<IActionResult> InicioFecha(string llave, string fecha)
        {
            if (llave != "3mVFuFFHGvVr1CvRDqYLA8HwB3nVXUk4")
            {
                return BadRequest();
            }

            var date = DateTime.ParseExact(fecha, "yyyy-MM-dd", CultureInfo.InvariantCulture);
            await _saldoBusiness.InicioDia(date);
            return Ok();
        }
        private void GenerarLog(string timestamp, string metodo, string text)
        {
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.ffffff");
            Console.WriteLine($"{current_time_formatt} INFO [{metodo} - {timestamp}] -> {text}");

        }
    }
}
