using Confluent.Kafka;
using Confluent.Kafka.Admin;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Kafka.Topics


{
    public class InicializadorTopicos: IInicializadorTopicos
    {
        private readonly IConfiguration _configuration;
        private readonly string[] _topics = { Topicos.SpeiOutPendienteHandlerKafka,Topicos.SpeiOutNotificationHandlerKafka,Topicos.SpeiOutRechazadoHandlerKafka,Topicos.SpeiOutReintentoHandlerKafka,Topicos.SpeiRetornoEstadoHandlerKafka,Topicos.SpeiInNotificationHandlerKafka,Topicos.SpeiRetornoAzulApiHandlerKafka,Topicos.PrevFraude_SolicitudValidada };
        private readonly ILogger<InicializadorTopicos> _logger;

        public InicializadorTopicos(ILogger<InicializadorTopicos> logger, IConfiguration configuration)
        {
            _logger = logger;
            _configuration= configuration;

        }

        public async Task CrearTopicos()
        {
            string _bootstrapServers = _configuration["Kafka:Server"];
            var config = new AdminClientConfig { BootstrapServers = _bootstrapServers };
         
            using (var adminClient = new AdminClientBuilder(config).Build())
            {
                try
                {
                    // Listar los tópicos disponibles
                    var metadata =  adminClient.GetMetadata(TimeSpan.FromSeconds(10));
                    var topics = metadata.Topics.Select(t => t.Topic).ToList();

                    foreach (var topic in _topics)
                    {
                        if (!topics.Contains(topic))
                        {
                            Console.WriteLine($"El tópico '{topic}' no existe.");
                            // Crear el tópico si no existe
                            var topicSpec = new TopicSpecification
                            {
                                Name = topic,
                                NumPartitions = 5,   
                                ReplicationFactor = 1 
                            };

                            // Crear el tópico en Kafka
                            await adminClient.CreateTopicsAsync(new[] { topicSpec });
                            Console.WriteLine($"El tópico '{topic}' ha sido creado.");

                            /* await adminClient.CreateTopicsAsync(
                                        _topics.Select(t => new TopicSpecification { Name = t, NumPartitions = 1, ReplicationFactor = 1 }).ToList()
                                    );*/
                        }
                      
                    }

                   
                    //await adminClient.CreateTopicsAsync(_topics.Select(t => new TopicSpecification { Name = t, NumPartitions = 1, ReplicationFactor = 1 }).ToList());

                    _logger.LogInformation("Tópicos de Kafka creados correctamente.");
                }
                catch (CreateTopicsException e)
                {
                    foreach (var result in e.Results)
                    {
                        _logger.LogWarning($"No se pudo crear el tópico '{result.Topic}': {result.Error.Reason}");
                    }
                }
            }
        }
    }
}
