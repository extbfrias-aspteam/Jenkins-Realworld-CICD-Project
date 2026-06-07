using Asp.Api.Azul.Core.Commons.Models.Dto;

namespace Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services
{
    public interface IContingencyServices
    {
        Task<bool> Validate<T>(int pbluId, int tipoPagoId, OrdenPagoDto ordenPago, string topicName, T eventMessage);
    }
}