using Confluent.Kafka;
using System.Text.Json;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Kafka.Producer
{
    public class KafkaProducerService
    {
        private readonly IProducer<string, string> _producer;
        private readonly ILogger<KafkaProducerService> _logger;

        public KafkaProducerService(IConfiguration configuration, ILogger<KafkaProducerService> logger)
        {
            _logger = logger;

            var config = new ProducerConfig
            {
                BootstrapServers = configuration["Kafka:Server"] ?? "localhost:9092",
                Acks = Acks.All
            };

            _producer = new ProducerBuilder<string, string>(config).Build();
        }

        public async Task EnviarMensajeAsync<T>(string topic, T mensaje,bool ignorarSerializado=false)
        {
            try
            {
                var key = Guid.NewGuid().ToString();
                var value = string.Empty;
                if (!ignorarSerializado)
                {
                    value = JsonSerializer.Serialize(mensaje);
                }
                else
                {
                    value = mensaje?.ToString();
                }


                var result = await _producer.ProduceAsync(topic, new Message<string, string>
                {
                    Key = key,
                    Value = value
                });

                _logger.LogInformation($"Mensaje enviado a [{topic}] con clave: {key} | Partición: {result.Partition} | Offset: {result.Offset}");
            }
            catch (Exception ex)
            {
                _logger.LogError($"Error al enviar mensaje a Kafka ({topic}): {ex.Message}");
            }
        }
    }
}
