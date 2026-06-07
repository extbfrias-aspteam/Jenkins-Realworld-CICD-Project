using System.Text.Json;
using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Kafka.Producer;
using Asp.Api.Azul.Kafka.Topics;
using Asp.Api.Azul.Models.Entities;
using Confluent.Kafka;

namespace Asp.Api.Azul.Kafka.Consumers.Services.PrevencionFraudes
{
    public class PrevFraude_SolicitudValidadaHandler : BackgroundService
    {

        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.PrevFraude_SolicitudValidada };
        private readonly ILogger<PrevFraude_SolicitudValidadaHandler> _logger;
        private readonly IConfiguration _configuration;
        private readonly IPagoBusiness _pagoBusiness;
        private readonly KafkaProducerService _kafkaProducer;
        private readonly IContingencyServices _contingencyServices;

        public PrevFraude_SolicitudValidadaHandler(ILogger<PrevFraude_SolicitudValidadaHandler> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IPagoBusiness pagoBusiness, KafkaProducerService kafkaProducer
            , IContingencyServices contingencyServices
                )
        {
            _logger = logger;
            _configuration = configuration;
            _inicializadorTopicos = inicializadorTopicos;
            _pagoBusiness = pagoBusiness;
            _kafkaProducer = kafkaProducer;
            _contingencyServices = contingencyServices;
        }


        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {

            int consumerCount = 4; // Número de consumidores en paralelo
            var tasks = new List<Task>();

            for (int i = 0; i < consumerCount; i++)
            {
                tasks.Add(Task.Run(() => ConsumeKafkaMessages(stoppingToken), stoppingToken));
            }

            await Task.WhenAll(tasks);
        }
        private async Task ConsumeKafkaMessages(CancellationToken stoppingToken)
        {
            await _inicializadorTopicos.CrearTopicos();
            string _bootstrapServers = _configuration["Kafka:Server"];
            var config = new ConsumerConfig
            {
                BootstrapServers = _bootstrapServers,
                GroupId = "prevencion-fraudes-eiyu",
                AutoOffsetReset = AutoOffsetReset.Latest,
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation($"Conectando al tópico {_topic[0]} con instancia {Task.CurrentId}...");

                    consumer.Subscribe(_topic);
                    _logger.LogInformation($"Conexión exitosa en instancia {Task.CurrentId}.");

                    while (!stoppingToken.IsCancellationRequested)
                    {
                        try
                        {
                            var consumeResult = consumer.Consume(stoppingToken);
                            _ = ProcessMessageAsync(consumeResult, consumer); // Ejecutar en segundo plano
                        }
                        catch (ConsumeException e)
                        {
                            _logger.LogError($"Error al consumir mensaje: {e.Error.Reason}");
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

        private async Task ProcessMessageAsync(ConsumeResult<Ignore, string> consumeResult, IConsumer<Ignore, string> consumer)
        {
            try
            {
              

                var data = JsonSerializer.Deserialize<DtoDatosOriginalesPago>(consumeResult.Message.Value);

                _logger.LogInformation($"\x1b[32m******** <PrevFraude_SolicitudValidadaHandler> Mensaje recibido  con clave de rastreo: {data.OrdenPago.CveRastreo ?? ""} ********\x1b[0m");

                var eventMessage = await _pagoBusiness.ProcesarPagoValidado(data);

                if (eventMessage != null)
                {
                    _logger.LogInformation($"\x1b[32m******** <PrevFraude_SolicitudValidadaHandler> La operación con clave de rastreo {data.OrdenPago.CveRastreo} se genero con éxito, se procede a enviar al motor de pagos mediante Kafka. ********\x1b[0m");
                    if (eventMessage.Proveedor == "ASP")
                    {
                        _logger.LogInformation($"Inicia envio del eventMessaje al topico {Topicos.SpeiOutAsp}");
                        await _kafkaProducer.EnviarMensajeAsync(Topicos.SpeiOutAsp, eventMessage);
                        _logger.LogInformation($"Finaliza envio del eventMessaje al topico {Topicos.SpeiOutAsp}");

                    }
                    else
                    {
                        _logger.LogInformation($"Inicia envio del eventMessaje al topico {Topicos.SpeiOutSies}");
                        
                        var isContingency = await _contingencyServices.Validate(data.IdPblu, 1, data.OrdenPago, Topicos.SpeiOutSies, eventMessage);
                        if (!isContingency)
                        {
                            await _kafkaProducer.EnviarMensajeAsync(Topicos.SpeiOutSies, eventMessage);
                            _logger.LogInformation($"Finaliza envio del eventMessaje al topico {Topicos.SpeiOutSies}");
                        }
                    }
                }
            

                consumer.Commit(consumeResult);
            }
            catch (Exception ex)
            {
                _logger.LogError($"Error procesando mensaje en paralelo: {ex.Message}");
            }
        }
    }
}