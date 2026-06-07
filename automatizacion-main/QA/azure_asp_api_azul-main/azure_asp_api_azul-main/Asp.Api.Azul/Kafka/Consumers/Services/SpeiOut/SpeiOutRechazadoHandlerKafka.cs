using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Topics;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;

namespace Asp.Api.Azul.Kafka.Consumers.Services.Spei_Out
{
    public class SpeiOutRechazadoHandlerKafka : BackgroundService
    {
        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.SpeiOutRechazadoHandlerKafka };
        private readonly ILogger<SpeiOutRechazadoHandlerKafka> _logger;
        private readonly IConfiguration _configuration;
        private readonly IPagoBusiness _pagoBusiness;

        public SpeiOutRechazadoHandlerKafka(ILogger<SpeiOutRechazadoHandlerKafka> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IPagoBusiness pagoBusiness
            )
        {
            _logger = logger;
            _configuration = configuration;
            _inicializadorTopicos = inicializadorTopicos;
            _pagoBusiness = pagoBusiness;
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
                GroupId = "spei-out-rechazado-group",
                //AutoOffsetReset = AutoOffsetReset
                AutoOffsetReset = AutoOffsetReset.Latest,
                //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation("Conectando al tópico SpeiOutRechazadoHandlerKafka...");
                    consumer.Subscribe(_topic);
                    _logger.LogInformation("Conexión exitosa SpeiOutRechazadoHandlerKafka.");
                    while (!stoppingToken.IsCancellationRequested)
                    {

                        try
                        {

                            var consumeResult = consumer.Consume(stoppingToken);

                            var data = JsonSerializer.Deserialize<SpeiOutRechazadoDto>(consumeResult.Message.Value);


                            _logger.LogInformation($"\x1b[32m******** <SpeiOutRechazadoHandlerKafka> Mensaje recibido  con clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");

                             await _pagoBusiness.PagoRechazado(data.ClaveRastreo, data.Descripcion);
                            _logger.LogInformation($"\x1b[32m******** <SpeiOutRechazadoHandlerKafka> Commit exitoso a clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");

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
