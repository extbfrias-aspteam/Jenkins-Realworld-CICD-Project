using Asp.Api.Azul.Entities.Business;

namespace Asp.Api.Azul.Services.KafkaPLD
{
    public interface IKafkaPldService
    {
        Task NotificarAbonoKafkaPLD(string url, KafkaConsumer data, int idPblu, string claveRastreo);
    }
}
