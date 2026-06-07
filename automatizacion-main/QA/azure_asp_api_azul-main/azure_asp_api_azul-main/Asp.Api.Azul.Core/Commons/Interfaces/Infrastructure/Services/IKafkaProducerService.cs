namespace Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services
{
    public interface IKafkaProducerService
    {
        Task SendMessageAsync(string bootstrapServers, string topic, string message);
    }
}