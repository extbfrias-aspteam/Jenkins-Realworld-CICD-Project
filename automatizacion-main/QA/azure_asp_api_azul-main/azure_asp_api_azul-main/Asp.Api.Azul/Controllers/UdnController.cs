using Asp.Api.Azul.Business.Authentication;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Repositorys.UdnRepository;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Utilities.GestionTokens;
using Asp.Cifrado.Services;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Text.Json;

namespace Asp.Api.Azul.Controllers
{
   
    public class UDNController : ControllerBase
    {
        private readonly IUdnRepository _udnRepository;
   
        
        private readonly IHttpContextAccessor _httpContext;
        private readonly IEncriptionService _encriptionService;
        private readonly IUserResolver _userResolver;
        private readonly IAspLogservice _aspLogservice;

        //añadir la interfaz IAspLogservice _aspLogservice en el constructor
        public UDNController(IUdnRepository udnRepository,IAspLogservice aspLogservice,IUserResolver userResolver, IHttpContextAccessor httpContext, IEncriptionService encriptionService)
        {
            this._udnRepository = udnRepository;
            this._httpContext = httpContext;
            this._encriptionService = encriptionService;
            this._userResolver = userResolver;
            this._aspLogservice = aspLogservice;
        }
        [HttpPost]
        [Route("/api/v1/udn/eyu_agregar_udn")]
        public async Task<ActionResult<DtoUdnId>> CrearUDN([FromBody] UdnRequest request)
        {
        
            var user = _userResolver.GetUser();
            int IdPblue= user.IdPblue;
            string Username = user.Username;          
            string firma = _httpContext.HttpContext.Request.Headers["Signature"].ToString();
            string llave = _httpContext.HttpContext.Request.Headers["Key"].ToString();
            var valorToken = _httpContext.HttpContext.Request.Headers["Authorization"].ToString();
            var jsonDescifrado = "null";
            try
            {
                string cuenta=request.cuenta_udn.ToString();
                if (!string.IsNullOrEmpty(valorToken))
                {

                  
                    string token = valorToken.ToString().Replace("Bearer ", "");
                    jsonDescifrado = _encriptionService.Decript(cuenta, llave);
                    UDN cuenta_udn = JsonSerializer.Deserialize<UDN>(jsonDescifrado);
                    if (!_encriptionService.VerificarFirma(jsonDescifrado, firma, IdPblue))
                    {
                        if (!_encriptionService.VerificarFirmaAlquimia(jsonDescifrado, firma, IdPblue))
                            throw new Exception("Firma no válida.");
                    }
                    Dictionary<string, object> res = await _udnRepository.InsertarUDN(cuenta_udn, IdPblue, Username);
                    
                    if ((bool)res["Estado"])
                    {
                        DtoUdnId nuevaUDN = new DtoUdnId()
                        {
                            Id= res["id_udn"].ToString(),
                            InfoAdicional= "Registro exitoso."
                        };
                        return Ok(nuevaUDN);
                    }
                    else{

                        await _aspLogservice.RegistraError("/api/v1/udn/eyu_agregar_udn", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, (Exception)res["Exception"], IdPblue, "");
                        return BadRequest("Los datos recibidos no son validos.");
                    }
                }
                else
                    return Unauthorized("Unauthorized");


            }
            catch (Exception ex)
            {

                string cuenta = request.cuenta_udn.ToString();
                // var jsonDescifrado = request == null ? "null" : _encriptionService.Decript(cuenta, llave);

                if (jsonDescifrado == "null")
                    await _aspLogservice.RegistraError("/api/v1/udn/eyu_agregar_udn", "POST", request, new { Signature = firma, Key = llave }, ex, IdPblue, "");
                else
                    await _aspLogservice.RegistraError("/api/v1/udn/eyu_agregar_udn", "POST", jsonDescifrado, new { Signature = firma, Key = llave }, ex, IdPblue, "");


                return StatusCode(StatusCodes.Status500InternalServerError, ex.Message);
            }

        }
    }
}