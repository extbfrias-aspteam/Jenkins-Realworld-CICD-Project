using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Topics;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;

namespace Asp.Api.Azul.Kafka.Consumers.Services.Spei_Out
{
    public class SpeiOutPendienteHandlerKafka : BackgroundService
    {
        private readonly IInicializadorTopicos _inicializadorTopicos;
        private readonly string[] _topic = { Topicos.SpeiOutPendienteHandlerKafka };
        private readonly ILogger<SpeiOutPendienteHandlerKafka> _logger;
        private readonly IConfiguration _configuration;
        private readonly IPagoBusiness _pagoBusiness;

        public SpeiOutPendienteHandlerKafka(ILogger<SpeiOutPendienteHandlerKafka> logger, IConfiguration configuration, IInicializadorTopicos inicializadorTopicos, IPagoBusiness pagoBusiness
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
        private async Task ProcessMessageAsync(ConsumeResult<Ignore, string> consumeResult, IConsumer<Ignore, string> consumer)
        {

            try
            {
                var data = JsonSerializer.Deserialize<SpeiOutPendienteDto>(consumeResult.Message.Value);


                _logger.LogInformation($"\x1b[32m******** <SpeiOutPendienteHandlerKafka> Mensaje recibido  con clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");

                bool actualizacionExitosa = await _pagoBusiness.PagoPendiente(data?.ClaveRastreo ?? "", data?.Descripcion ?? "");
                if (actualizacionExitosa)
                {
                    _logger.LogInformation($"\x1b[32m******** <SpeiOutPendienteHandlerKafka> Commit exitoso a clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");
                }
                else
                {
                    _logger.LogWarning($"\x1b[33m******** <SpeiOutPendienteHandlerKafka> El mensaje ya fue procesado anteriormente. Se hará commit de todas formas. ********\x1b[0m");
                }

            }
            catch (Exception ex)
            {
                _logger.LogError($"Error procesando mensaje: {ex.Message}");
            }

            consumer.Commit(consumeResult);


        }
        private async Task ConsumeKafkaMessages(CancellationToken stoppingToken)
        {
            await _inicializadorTopicos.CrearTopicos();
            string _bootstrapServers = _configuration["Kafka:Server"];
            var config = new ConsumerConfig
            {
                BootstrapServers = _bootstrapServers,
                GroupId = "spei-out-pendiente-group",
                //AutoOffsetReset = AutoOffsetReset
                AutoOffsetReset = AutoOffsetReset.Latest,
                //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
                EnableAutoCommit = false
            };

            using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
            {
                try
                {
                    _logger.LogInformation("Conectando al tópico SpeiOutPendienteHandlerKafka...");
                    consumer.Subscribe(_topic);
                    _logger.LogInformation("Conexión exitosa.");
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
        //private async Task ConsumeKafkaMessages(CancellationToken stoppingToken)
        //{

        //    string _bootstrapServers = _configuration["Kafka:Server"];
        //    var config = new ConsumerConfig
        //    {
        //        BootstrapServers = _bootstrapServers,
        //        GroupId = "spei-out-pendiente-group",
        //        //AutoOffsetReset = AutoOffsetReset
        //        AutoOffsetReset = AutoOffsetReset.Latest,
        //        //Latest Esto garantiza que el consumidor solo lea los mensajes nuevos despues del ultimo (commit) realizado.
        //        EnableAutoCommit = false
        //    };

        //    using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
        //    {
        //        try
        //        {
        //            _logger.LogInformation("Conectando al tópico SpeiOutPendienteHandlerKafka...");
        //            consumer.Subscribe(_topic);
        //            _logger.LogInformation("Conexión exitosa.");
        //            while (!stoppingToken.IsCancellationRequested)
        //            {
        //                try
        //                {
        //                    var consumeResult = consumer.Consume(stoppingToken);


        //                    if (consumeResult != null)
        //                    {


        //                        try
        //                        {
        //                            var data = JsonSerializer.Deserialize<SpeiOutPendienteDto>(consumeResult.Message.Value);


        //                            _logger.LogInformation($"\x1b[32m******** <SpeiOutPendienteHandlerKafka> Mensaje recibido  con clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");

        //                            bool actualizacionExitosa = await _pagoBusiness.PagoPendiente(data?.ClaveRastreo ?? "", data?.Descripcion ?? "");
        //                            if (actualizacionExitosa)
        //                            {
        //                                _logger.LogInformation($"\x1b[32m******** <SpeiOutPendienteHandlerKafka> Commit exitoso a clave de rastreo: {data?.ClaveRastreo ?? ""} ********\x1b[0m");
        //                            }
        //                            else
        //                            {
        //                                _logger.LogWarning($"\x1b[33m******** <SpeiOutPendienteHandlerKafka> El mensaje ya fue procesado anteriormente. Se hará commit de todas formas. ********\x1b[0m");
        //                            }

        //                        }
        //                        catch (Exception ex)
        //                        {
        //                            _logger.LogError($"Error procesando mensaje: {ex.Message}");
        //                        }

        //                        consumer.Commit(consumeResult);


        //                    }







        //                }
        //                catch (ConsumeException e)
        //                {
        //                    _logger.LogError($"Error al consumir el mensaje: {e.Error.Reason}");
        //                }
        //                catch (Exception ex)
        //                {

        //                    _logger.LogError($"Error de Kafka [1]: {ex.Message}");
        //                }
        //            }
        //        }
        //        catch (Exception ex)
        //        {
        //            _logger.LogError($"Error de Kafka [2]: {ex.Message}");
        //        }

        //    }
        //}
    }
}
