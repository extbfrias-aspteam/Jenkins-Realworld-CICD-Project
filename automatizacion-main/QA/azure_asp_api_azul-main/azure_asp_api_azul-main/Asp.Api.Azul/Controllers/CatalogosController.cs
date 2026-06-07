using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Repositorys.CatalogoRepository;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Utilities.GestionTokens;
using Microsoft.AspNetCore.Mvc;

namespace Asp.Api.Azul.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CatalogoController : ControllerBase
    {
 
        private readonly ICatalogoRepository _catalogoRepository;

        public CatalogoController( ICatalogoRepository catalogoRepository)
        {
            _catalogoRepository = catalogoRepository;

        }
        [HttpGet]
        [Route("/api/v1/catalogos/eyu_banco")]
        public async Task<ActionResult<List<Banco>>>  GetBanco()
        {
            try
            {
                var catalogoBanco = await _catalogoRepository.GetBanco();
                var bancos = new List<Banco>();
                foreach (var catalogoBancoItem in catalogoBanco)
                {
                    var bancosResponse = new Banco();
                    bancosResponse.IdBanco = catalogoBancoItem.IdBanco;
                    bancosResponse.Descripcion = catalogoBancoItem.Descripcion;
                    bancos.Add(bancosResponse);
                }
                //_aspLogservice.RegistraCatalogoLog("/api/v1/catalogos/eyu_banco", "GET", new { }, "OkObjectResult", new OkObjectResult(bancos), new { }, new { },0);
                return new OkObjectResult(bancos);
            }
            catch (Exception ex)
            {
                //_aspLogservice.RegistraError("/api/v1/catalogos/eyu_banco", "GET", new { }, new { }, ex, 0);
                return StatusCode(500, ex.Message);
            }
        }
    }
}