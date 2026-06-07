using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Services
{
    public interface IAspApiClient
    {
        Task<List<DtoCtaRefFinalResp>> EnviarAltaRefCtaExpedienteFinal(string jsonEnviar, int idPblu, string clabe, string timestamp="");
    }
}
