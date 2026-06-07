using Asp.Api.Azul.Business.Abono;
using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Kafka.Dtos.SpeiIn;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Dtos.SpeiRetornos;
using Asp.Api.Azul.Kafka.Topics;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;

namespace Asp.Api.Azul.Kafka.Consumers.Services.Spei_Retornos
{
    public class SpeiRetornoAzulApiHandlerKafka : BackgroundService
    {
        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.SpeiRetornoAzulApiHandlerKafka };
        private readonly ILogger<SpeiRetornoAzulApiHandlerKafka> _logger;
        private readonly IConfiguration _configuration;
        private readonly IAbonoBusiness _abonoBusiness;

        public SpeiRetornoAzulApiHandlerKafka(ILogger<SpeiRetornoAzulApiHandlerKafka> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IAbonoBusiness abonoBusiness
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
                GroupId = "spei-out-retornos-azul-group",
                //AutoOffsetReset = AutoOffsetReset
                AutoOffsetReset = AutoOffsetReset.Latest,
                //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation("Conectando al tópico SpeiRetornoAzulApiHandlerKafka...");
                    consumer.Subscribe(_topic);
                    _logger.LogInformation("Conexión exitosa SpeiRetornoAzulApiHandlerKafka.");
                    while (!stoppingToken.IsCancellationRequested)
                    {

                        try
                        {
                            var consumeResult = consumer.Consume(stoppingToken);
                            var data = JsonSerializer.Deserialize<SpeiInNotificationDto>(consumeResult.Message.Value);
                            _logger.LogInformation($"\x1b[32m******** <SpeiRetornoAzulApiHandlerKafka> Mensaje recibido: {data?.CveRastreo ?? ""} ********\x1b[0m");

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

                            await _abonoBusiness.RecibeRetorno(ordenAbono, data.Firma);
                            _logger.LogInformation($"\x1b[32m******** <SpeiRetornoAzulApiHandlerKafka> Commit exitoso: {data?.CveRastreo ?? ""} ********\x1b[0m");
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
