using Asp.Api.Azul.Business.Abono;
using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Dtos.SpeiRetornos;
using Asp.Api.Azul.Kafka.Topics;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;

namespace Asp.Api.Azul.Kafka.Consumers.Services.Spei_Retornos
{
    public class SpeiRetornoEstadoHandlerKafka : BackgroundService
    {
        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.SpeiRetornoEstadoHandlerKafka };
        private readonly ILogger<SpeiRetornoEstadoHandlerKafka> _logger;
        private readonly IConfiguration _configuration;
        private readonly IAbonoBusiness _abonoBusiness;

        public SpeiRetornoEstadoHandlerKafka(ILogger<SpeiRetornoEstadoHandlerKafka> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IAbonoBusiness abonoBusiness
            )
        {
            _logger = logger;
            _configuration = configuration;
            _inicializadorTopicos = inicializadorTopicos;
            _abonoBusiness = abonoBusiness;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            var task = Task.Run(async () => await ConsumeKafkaMessages(stoppingToken), stoppingToken);
            await task;

        }
        private async Task ConsumeKafkaMessages(CancellationToken stoppingToken)
        {

            await _inicializadorTopicos.CrearTopicos();
            string _bootstrapServers = _configuration["Kafka:Server"];
            var config = new ConsumerConfig
            {
                BootstrapServers = _bootstrapServers,
                GroupId = "spei-out-retornos-cambio-estado-group",
                //AutoOffsetReset = AutoOffsetReset
                AutoOffsetReset = AutoOffsetReset.Latest,
                //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation("Conectando al tópico SpeiRetornoEstadoHandlerKafka...");
                    consumer.Subscribe(_topic);
                    _logger.LogInformation("Conexión exitosa SpeiRetornoEstadoHandlerKafka.");
                    while (!stoppingToken.IsCancellationRequested)
                    {

                        try
                        {

                            var consumeResult = consumer.Consume(stoppingToken);

                            var data = JsonSerializer.Deserialize<SpeiRetornoEstadoDto>(consumeResult.Message.Value);


                            _logger.LogInformation($"\x1b[32m******** <SpeiRetornoEstadoHandlerKafka> Mensaje recibido  con clave de rastreo: {data.ClaveRastreo} ********\x1b[0m");
                            await _abonoBusiness.CambioEstadoRetorno(data.ClaveRastreo, data.Estado, string.Empty, DateTime.Now, data.DevolucionId, data.FechaOperacion);

                            _logger.LogInformation($"\x1b[32m******** <SpeiRetornoEstadoHandlerKafka> Commit exitoso a clave de rastreo: {data.ClaveRastreo} ********\x1b[0m");

                            consumer.Commit(consumeResult);


                        }
                        catch (ConsumeException e)
                        {

                            _logger.LogError($"Error al consumir el mensaje: {e.Error.Reason}");
                        }
                        catch (Exception ex)
                        {

                            _logger.LogError($"Error de Kafka [1]: {ex.Message}");
                        }

                    }

                }
                catch (Exception ex)
                {

                    _logger.LogError($"Error de Kafka [2]: {ex.Message}");
                }

            }
        }
    }
}
