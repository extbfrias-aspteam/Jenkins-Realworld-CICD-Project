using Asp.Api.Azul.Business.Authentication;
using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Utils;
using System.Numerics;
using System.Text.Json;

namespace Asp.Api.Azul.Services.KafkaPLD
{
    public class KafkaPldService : IKafkaPldService
    {
        private readonly HttpClient _apiClient;
        private readonly IAspLogservice _aspLogservice;
        private readonly LoggerHelper _logger;
        public KafkaPldService(HttpClient apiClient, IAspLogservice aspLogservice, LoggerHelper logger)
        {
            _aspLogservice = aspLogservice;
            _apiClient = apiClient;
            _logger= logger;
            _apiClient.Timeout = TimeSpan.FromSeconds(30);

        }

        public async Task NotificarAbonoKafkaPLD(string url, KafkaConsumer data,int idPblu,string claveRastreo)
        {

            var content = new StringContent(JsonSerializer.Serialize(data), System.Text.Encoding.UTF8, "application/json");
            var contentString = await content.ReadAsStringAsync();
            try {
                var response = await _apiClient.PostAsync(url, content);


                if (response.IsSuccessStatusCode)
                {
                    // La solicitud fue exitosa (código de estado 2xx)
                    // Puedes realizar acciones adicionales aquí si es necesario
                    _logger.LogInformation($"Solicitud exitosa PLD: {response.StatusCode} - {response.ReasonPhrase}");

                }
                else
                {
                    Exception ex = null;
                    // La solicitud no fue exitosa, puedes manejar el error aquí
                    _logger.LogInformation($"Error al enviar POST al servicio PLD Kafka: {response.StatusCode} - {response.ReasonPhrase} - claveRastreo: {claveRastreo}");
                    // También puedes leer el contenido de la respuesta si es necesario
                    string responseBody = await response.Content.ReadAsStringAsync();
                    _logger.LogInformation($"Contenido de la respuesta: {responseBody}");
                    await _aspLogservice.RegistraError(url, "POST", responseBody, new { }, ex, idPblu, claveRastreo);

                }
            }
            catch (TaskCanceledException ex)
            {

                _logger.LogError($"La tarea fue cancelada debido a un timeout. - claveRastreo: {claveRastreo}",ex);

            }
            catch (Exception ex)
            {
                _logger.LogError($"Error al enviar POST al servicio PLD Kafka claveRastreo: {claveRastreo}",ex);

                await _aspLogservice.RegistraError(url, "POST", content, new { }, ex, idPblu, claveRastreo);
            }


        }
    }
}
