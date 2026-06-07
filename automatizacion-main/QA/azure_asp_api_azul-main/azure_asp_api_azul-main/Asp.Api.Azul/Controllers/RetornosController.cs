using Asp.Api.Azul.Business.Retornos;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Utilities.GestionTokens;
using Asp.Cifrado.Services;
using Microsoft.AspNetCore.Mvc;
using System.Numerics;
using System.Text.Json;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Asp.Api.Azul.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class RetornosController : ControllerBase
    {
        private readonly IRetornoBusiness _retornoBusiness;
        private readonly IUserResolver _userResolver;
        private readonly IHttpContextAccessor _httpContext;
        private readonly IEncriptionService _encriptionService;
        private readonly IConfiguration _configuration;
        private readonly IAspLogservice _aspLogservice;
        public RetornosController(IRetornoBusiness retornoBusiness, IUserResolver userResolver, IHttpContextAccessor httpContext, IEncriptionService encriptionService, IConfiguration configuration, IAspLogservice aspLogservice)
        {
            _retornoBusiness = retornoBusiness;
            _userResolver = userResolver;
            _httpContext = httpContext;
            _encriptionService = encriptionService;
            _configuration = configuration;
            _aspLogservice=aspLogservice;
        }

        // POST api/<RetornosController>
        [HttpPost("retornar-abono")]
       // public void Post([FromBody] string value)
         public async Task<ActionResult<string>> RetornoAbono([FromBody] RetornoCifradoRequest request)
        {
                DateTime currentTime = DateTime.Now;
                string IDENTIFICADOR = "ID_" + currentTime.ToString("yyyyMMddHHmmssffffff");
                GenerarLog(IDENTIFICADOR, "RetornoAbono", "Inicio RetornoAbono");

                var tiempoLlegada = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff");
                var user = _userResolver.GetUser();
                int idPblu = user.IdPblue;
                int cert = user.IdCertificado;

              
                GenerarLog(IDENTIFICADOR, "RetornoAbono", $"ID del pblu {idPblu}");
                string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
                string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
                string claveRastreo = string.Empty;
            try
            {
                string tokenPortal = _httpContext.HttpContext.Request.Headers["token_portal"].ToString();
                var tokenPortalValidar = _configuration["Portal:Token"];
                var jsonDescifrado = "null";

                var isPortal = !string.IsNullOrEmpty(tokenPortal) && tokenPortal == tokenPortalValidar;
                jsonDescifrado = _encriptionService.Decript(request.Retorno, llave, IDENTIFICADOR);
                //print
                GenerarLog(IDENTIFICADOR, "RetornoAbono", $"JSON descifrado->{jsonDescifrado}");
                if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, idPblu, isPortal, IDENTIFICADOR))
                {
                    if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, idPblu))
                        throw new ErrorFirma("Firma no válida.");
                }

                var retorno = JsonSerializer.Deserialize<RetornoRequest>(jsonDescifrado);
                claveRastreo = retorno.ClaveRastreo;
                await _retornoBusiness.InsertarRetornoAsync(retorno, idPblu,IDENTIFICADOR);
                GenerarLog(IDENTIFICADOR, "RetornoAbono", $"Retorno registrado exitosamente");
                return Ok("Retorno registrado exitosamente");
            }
            catch (Exception ex) {
                GenerarLog(IDENTIFICADOR, "RetornoAbono", $"Error: {ex.StackTrace}");
                await _aspLogservice.RegistraError("/api/v1/retornos/retornar-abono", "POST", request, new { Signature = firma, Key = llave }, ex, idPblu, claveRastreo);
                return BadRequest(ex.Message);
            }
            
        }
        private void GenerarLog(string timestamp, string metodo, string text)
        {
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.ffffff");
            Console.WriteLine($"{current_time_formatt} INFO [{metodo} - {timestamp}] -> {text}");

        }

    }
}
