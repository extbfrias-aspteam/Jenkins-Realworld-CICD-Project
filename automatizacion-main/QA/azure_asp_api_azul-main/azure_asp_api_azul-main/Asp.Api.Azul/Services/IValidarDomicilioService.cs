using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Services
{
    public interface IValidarDomicilioService
    {
        void ValidarDomicilio(DtoCtaExpedienteDomicilio domicilio, Char tipoPersona);
    }
}
