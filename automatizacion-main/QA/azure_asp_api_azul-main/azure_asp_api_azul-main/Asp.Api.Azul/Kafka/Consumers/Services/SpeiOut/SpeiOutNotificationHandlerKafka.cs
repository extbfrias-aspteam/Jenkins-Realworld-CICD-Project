using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Kafka.Dtos.SpeiIn;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Topics;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;

namespace Asp.Api.Azul.Kafka.Consumers.Services.Spei_Out
{
    public class SpeiOutNotificationHandlerKafka : BackgroundService
    {
        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.SpeiOutNotificationHandlerKafka };
        private readonly ILogger<SpeiOutNotificationHandlerKafka> _logger;
        private readonly IConfiguration _configuration;
        private readonly IPagoBusiness _pagoBusiness;

        public SpeiOutNotificationHandlerKafka(ILogger<SpeiOutNotificationHandlerKafka> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IPagoBusiness pagoBusiness
            )
        {
            _logger = logger;
            _configuration = configuration;
            _inicializadorTopicos = inicializadorTopicos;
            _pagoBusiness = pagoBusiness;
        }

        /*protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            var task = Task.Run(async () => await ConsumeKafkaMessages(stoppingToken), stoppingToken);
            await task;

        }*/
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
                GroupId = "spei-out-notificacion-cambio-estado-group",
                //AutoOffsetReset = AutoOffsetReset
                AutoOffsetReset = AutoOffsetReset.Latest,
                //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation($"Conectando al tópico SpeiOutNotificationHandlerKafka con instancia {Task.CurrentId}...");
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
        /*  private async Task ConsumeKafkaMessages(CancellationToken stoppingToken)
          {

              await _inicializadorTopicos.CrearTopicos();
              string _bootstrapServers = _configuration["Kafka:Server"];
              var config = new ConsumerConfig
              {
                  BootstrapServers = _bootstrapServers,
                  GroupId = "spei-out-notificacion-cambio-estado-group",
                  //AutoOffsetReset = AutoOffsetReset
                  AutoOffsetReset = AutoOffsetReset.Latest,
                  //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
                  EnableAutoCommit = false
              };

              using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
              {
                  try
                  {
                      _logger.LogInformation("Conectando al tópico SpeiOutNotificationHandlerKafka...");
                      consumer.Subscribe(_topic);
                      _logger.LogInformation("Conexión exitosa SpeiOutNotificationHandlerKafka.");
                      while (!stoppingToken.IsCancellationRequested)
                      {

                          try
                          {

                              var consumeResult = consumer.Consume(stoppingToken);

                              var data = JsonSerializer.Deserialize<SpeiOutNotificationDto>(consumeResult.Message.Value);


                              _logger.LogInformation($"\x1b[32m******** <SpeiOutNotificationHandlerKafka> Mensaje recibido  con clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");

                              bool actualizacionExitosa = await _pagoBusiness.PagoCambioEstado(data.ClaveRastreo, int.Parse(data.Estado), data.Causa, data.FechaOperacion, data.IdCausaDevolucion);

                              if (actualizacionExitosa)
                              {
                                  _logger.LogInformation($"\x1b[32m******** <SpeiOutNotificationHandlerKafka> Commit exitoso a clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");
                              }
                              else
                              {
                                  _logger.LogWarning($"\x1b[33m******** <SpeiOutNotificationHandlerKafka> El mensaje ya fue procesado anteriormente. Se hará commit de todas formas. ********\x1b[0m");
                              }

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

          }*/

        private async Task ProcessMessageAsync(ConsumeResult<Ignore, string> consumeResult, IConsumer<Ignore, string> consumer)
        {
            try
            {
                var data = JsonSerializer.Deserialize<SpeiOutNotificationDto>(consumeResult.Message.Value);


                _logger.LogInformation($"\x1b[32m******** <SpeiOutNotificationHandlerKafka> Mensaje recibido  con clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");

                bool actualizacionExitosa = await _pagoBusiness.PagoCambioEstado(data.ClaveRastreo, int.Parse(data.Estado), data.Causa, data.FechaOperacion, data.IdCausaDevolucion);

                if (actualizacionExitosa)
                {
                    _logger.LogInformation($"\x1b[32m******** <SpeiOutNotificationHandlerKafka> Commit exitoso a clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");
                }
                else
                {
                    _logger.LogWarning($"\x1b[33m******** <SpeiOutNotificationHandlerKafka> El mensaje ya fue procesado anteriormente. Se hará commit de todas formas. ********\x1b[0m");
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
