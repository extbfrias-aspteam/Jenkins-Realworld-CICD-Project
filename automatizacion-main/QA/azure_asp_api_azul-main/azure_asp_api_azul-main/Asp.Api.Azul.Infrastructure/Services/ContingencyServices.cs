using System.Text;
using System.Text.Json;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Infrastructure.Configurations;
using Asp.Api.Azul.Infrastructure.Utils;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Const = Asp.Api.Azul.Core.Commons.Constants.GeneralConstants;

namespace Asp.Api.Azul.Infrastructure.Services
{
    public class ContingencyServices : IContingencyServices
    {
        private readonly LoggerHelper _logger;
        private readonly IAspEiyuMonitorLoggingService _aspEiyuMonitorLoggingService;
        private readonly MicroServiceConfig microService;
        private readonly HttpClient _httpClient;
        private readonly IKafkaProducerService _kafkaProducerService;
        private string kafkaServer;

        public ContingencyServices(
            LoggerHelper logger,
            IAspEiyuMonitorLoggingService aspEiyuMonitorLoggingService, 
            IOptions<List<MicroServiceConfig>> apisOptions,
            IHttpClientFactory httpClientFactory,
            IKafkaProducerService kafkaProducerService,
            IConfiguration configuration
            )
        {
            _logger = logger;
            _aspEiyuMonitorLoggingService = aspEiyuMonitorLoggingService;
            _kafkaProducerService = kafkaProducerService;
            kafkaServer = configuration["Kafka:Server"] 
                ?? throw new Exception("Kafka server not configured");

            microService = apisOptions.Value.Where(x=>x.Code== Const.MicroServicesCode.CONTINGENCY).FirstOrDefault() 
                ?? throw new Exception("Not found MicroService:Contingency");

            _httpClient = httpClientFactory.CreateClient("Contingency");
            _httpClient.Timeout = TimeSpan.FromMilliseconds(microService.TimeoutMs);
        }

        public async Task<bool> Validate<T>(int pbluId, int tipoPagoId, OrdenPagoDto ordenPago, string topicName, T eventMessage)
        {
            var requestPayload = new
            {
                PbluId = pbluId,
                TipoPagoId = tipoPagoId,
                CveRastreo = ordenPago.CveRastreo,
            };

            var json = JsonSerializer.Serialize(requestPayload);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            try
            {
                _logger.LogInformation($"[CONTINGENCY 2.0] - Request to {microService.Endpoint} TimeOutMs={microService.TimeoutMs} - CveRastreo: {ordenPago.CveRastreo}");

                var response = await _httpClient.PostAsync(microService.Endpoint, content);

                var responseContent = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                {
                    _logger.LogInformation($"[CONTINGENCY 2.0] - API Error: {response.StatusCode} - {responseContent} - CveRastreo: {ordenPago.CveRastreo}");
                    _=_aspEiyuMonitorLoggingService.SendErrorGeneric($"API Error: {response.StatusCode} - {responseContent}", responseContent);
                    return false;
                }

                var apiResponse = JsonSerializer.Deserialize<ApiResponse<bool>>(responseContent, new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

                string apiResponseStr = JsonSerializer.Serialize(apiResponse);
                _logger.LogInformation($"[CONTINGENCY 2.0] - CveRastreo: {ordenPago.CveRastreo} - StatusCode: {response.StatusCode} - Response: {apiResponseStr}");

                var result = apiResponse?.Data ?? false;

                if (result)
                {
                    var eventMesageContingency = new
                    {
                        PbluId = pbluId,
                        TipoPagoId = tipoPagoId,
                        TopicName = topicName,
                        OrdenPago = ordenPago,
                        EventMessage = JsonSerializer.Serialize(eventMessage),
                    };
                    string eventMesageContingencyStr = JsonSerializer.Serialize(eventMesageContingency);
                    await PublishMessage(eventMesageContingencyStr);
                }

                return result;
            }
            catch (Exception ex)
            {
                _ = _aspEiyuMonitorLoggingService.SendErrorGeneric(ex.Message, ex.ToString(), requestPayload);
                _logger.LogError("Ocurrio un error Contingency Service",ex);
                return false;
            }
        }

        private async Task PublishMessage(string eventMessage)
        {
            _logger.LogInformation($"[CONTINGENCY 2.0] Publishing message to Kafka topic {Const.KafkaTopics.CONTINGENCY_ACTIVE} with content: {eventMessage}");
            await _kafkaProducerService.SendMessageAsync(kafkaServer, Const.KafkaTopics.CONTINGENCY_ACTIVE, eventMessage);
        }
    }
}