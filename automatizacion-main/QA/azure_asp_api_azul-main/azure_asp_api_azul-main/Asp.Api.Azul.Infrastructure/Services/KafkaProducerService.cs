using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;

namespace Asp.Api.Azul.Infrastructure.Services
{
    public class KafkaProducerService : IKafkaProducerService
    {
        private readonly ILogger _logger;

        public KafkaProducerService(ILoggerFactory loggerFactory)
        {
            _logger = loggerFactory.CreateLogger("KafkaProducerService");
        }

        public async Task SendMessageAsync(string bootstrapServers, string topic, string message)
        {
            var config = new ProducerConfig
            {
                BootstrapServers = bootstrapServers
            };

            using var producer = new ProducerBuilder<string, string>(config).Build();

            try
            {
                var result = await producer.ProduceAsync(topic, new Message<string, string>
                {
                    Key = Guid.NewGuid().ToString(),
                    Value = message,
                });

#if DEBUG
                _logger.LogInformation("Message is sent: {Message} (Partition: {Partition}, Offset: {Offset})", message, result.Partition, result.Offset);
#endif
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error sending message to Kafka topic {Topic}", topic);
            }
        }
    }
}