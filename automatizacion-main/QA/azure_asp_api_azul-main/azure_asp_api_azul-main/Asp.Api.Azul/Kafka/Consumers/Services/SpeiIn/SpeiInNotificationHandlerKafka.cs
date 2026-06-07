using Asp.Api.Azul.Business.Abono;
using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Kafka.Dtos.SpeiIn;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Topics;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;

namespace Asp.Api.Azul.Kafka.Consumers.Services.SpeiIn
{
    public class SpeiInNotificationHandlerKafka : BackgroundService
    {
        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.SpeiInNotificationHandlerKafka };
        private readonly ILogger<SpeiInNotificationHandlerKafka> _logger;
        private readonly IConfiguration _configuration;
        private readonly IAbonoBusiness _abonoBusiness;

        public SpeiInNotificationHandlerKafka(ILogger<SpeiInNotificationHandlerKafka> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IAbonoBusiness abonoBusiness
            )
        {
            _logger = logger;
            _configuration = configuration;
            _inicializadorTopicos = inicializadorTopicos;
            _abonoBusiness = abonoBusiness;
        }

        /*  protected override async Task ExecuteAsync(CancellationToken stoppingToken)
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
                  GroupId = "spei-in-abono-group",
                  AutoOffsetReset = AutoOffsetReset.Latest,
                  EnableAutoCommit = false
              };

              using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
              {
                  try
                  {
                      _logger.LogInformation("Conectando al tópico SpeiInNotificationHandlerKafka...");
                      consumer.Subscribe(_topic);
                      _logger.LogInformation("Conexión exitosa SpeiInNotificationHandlerKafka.");

                      var processingTasks = new List<Task>(); 

                      while (!stoppingToken.IsCancellationRequested)
                      {
                          try
                          {
                              var consumeResult = consumer.Consume(stoppingToken);


                              var task = Task.Run(async () =>
                              {
                                  try
                                  {
                                      var data = JsonSerializer.Deserialize<SpeiInNotificationDto>(consumeResult.Message.Value);

                                      _logger.LogInformation($"\x1b[32m******** <SpeiInNotificationHandlerKafka> Mensaje recibido con clave de rastreo: {data?.CveRastreo ?? ""} ********\x1b[0m");

                                      var ordenAbono = new OrdenAbono
                                      {
                                          CuentaDestino = data.CuentaDestino,
                                          CveRastreo = data.CveRastreo,
                                          RefCob = data.RefCob,
                                          FolioPaquete = data.FolioPaquete,
                                          IdTipoCuentaDestino = data.IdTipoCuentaDestino,
                                          IdTipoPago = data.IdTipoPago,
                                          BancoOrigen = data.BancoOrigen,
                                          Clabe = data.Clabe,
                                          ConceptoPago = data.ConceptoPago,
                                          RefNum = data.RefNum,
                                          Iva = data.Iva,
                                          CausaDev = data.CausaDev,
                                          CuentaConcentradora = data.CuentaConcentradora,
                                          CuentaReferencia = data.CuentaReferencia,
                                          FechaCaptura = data.FechaCaptura,
                                          FechaOperacion = data.FechaOperacion,
                                          Folio = data.Folio,
                                          Monto = data.Monto,
                                          NombreDestino = data.NombreDestino,
                                          NombreOrigen = data.NombreOrigen,
                                          RfcDestino = data.RfcDestino,
                                          RfcOrigen = data.RfcOrigen,
                                          Estado = 22,
                                          Uuid = data.Uuid,
                                          IdRetiro = data.IdRetiro
                                      };

                                      await _abonoBusiness.RecibeAbono(ordenAbono, data.Firma);

                                      _logger.LogInformation($"\x1b[32m******** <SpeiInNotificationHandlerKafka> Commit exitoso a clave de rastreo: {data?.CveRastreo ?? ""} ********\x1b[0m");

                                      consumer.Commit(consumeResult);
                                  }
                                  catch (Exception ex)
                                  {
                                      _logger.LogError($"Error procesando mensaje en paralelo: {ex.Message}");
                                  }
                              }, stoppingToken);

                              processingTasks.Add(task);

                              processingTasks.RemoveAll(t => t.IsCompleted);
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

                      await Task.WhenAll(processingTasks);

                  }
                  catch (Exception ex)
                  {
                      _logger.LogError($"Error de Kafka [2]: {ex.Message}");
                  }
              }
          }

          */


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
                GroupId = "spei-in-abono-group",
                AutoOffsetReset = AutoOffsetReset.Latest,
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation($"Conectando al tópico SpeiInNotificationHandlerKafka con instancia {Task.CurrentId}...");
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
                var data = JsonSerializer.Deserialize<SpeiInNotificationDto>(consumeResult.Message.Value);
                _logger.LogInformation($"\x1b[32m******** <SpeiInNotificationHandlerKafka> Mensaje recibido: {data?.CveRastreo ?? ""} ********\x1b[0m");

                var ordenAbono = new OrdenAbono
                {
                    CuentaDestino = data.CuentaDestino,
                    CveRastreo = data.CveRastreo,
                    RefCob = data.RefCob,
                    FolioPaquete = data.FolioPaquete,
                    IdTipoCuentaDestino = data.IdTipoCuentaDestino,
                    IdTipoPago = data.IdTipoPago,
                    BancoOrigen = data.BancoOrigen,
                    Clabe = data.Clabe,
                    ConceptoPago = data.ConceptoPago,
                    RefNum = data.RefNum,
                    Iva = data.Iva,
                    CausaDev = data.CausaDev,
                    CuentaConcentradora = data.CuentaConcentradora,
                    CuentaReferencia = data.CuentaReferencia,
                    FechaCaptura = data.FechaCaptura,
                    FechaOperacion = data.FechaOperacion,
                    Folio = data.Folio,
                    Monto = data.Monto,
                    NombreDestino = data.NombreDestino,
                    NombreOrigen = data.NombreOrigen,
                    RfcDestino = data.RfcDestino,
                    RfcOrigen = data.RfcOrigen,
                    Estado = 22,
                    Uuid = data.Uuid,
                    IdRetiro = data.IdRetiro
                };

                await _abonoBusiness.RecibeAbono(ordenAbono, data.Firma);
                _logger.LogInformation($"\x1b[32m******** <SpeiInNotificationHandlerKafka> Commit exitoso: {data?.CveRastreo ?? ""} ********\x1b[0m");

                consumer.Commit(consumeResult);
            }
            catch (Exception ex)
            {
                _logger.LogError($"Error procesando mensaje en paralelo: {ex.Message}");
            }
        }

    }
}
