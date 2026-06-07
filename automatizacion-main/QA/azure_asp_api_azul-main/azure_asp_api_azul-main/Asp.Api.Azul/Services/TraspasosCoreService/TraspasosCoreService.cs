using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Utils;
using Asp.Api.Azul.Services.JwtService;
using Asp.Api.Azul.Traspasos.Core.domain.dtos;
using Asp.Api.Azul.Traspasos.Core.domain.request;
using Asp.Api.Azul.Traspasos.Core.domain.response;
using System.Text.Json;

namespace Asp.Api.Azul.Services.TraspasosCoreService
{
    public interface ITraspasosCoreService
    {
        Task EnviarNotificaciónAbono(NotificacionAbonoTraspasoDto traspaso);
    }
    public class TraspasosCoreService : ITraspasosCoreService
    {
        private readonly IConfiguration _configuration;
        private readonly IJwtIssuerService _jwtIssuerService;
        private readonly IHttpClientFactory _httpClientFactory;
        private readonly LoggerHelper _logger;
        private readonly string _urlNotificacionAbonoCore;

        public TraspasosCoreService(IConfiguration configuration, LoggerHelper logger, IJwtIssuerService jwtIssuerService, IHttpClientFactory httpClientFactory)
        {
            _configuration = configuration;
            _logger = logger;
            _jwtIssuerService = jwtIssuerService;
            _httpClientFactory = httpClientFactory;
            _urlNotificacionAbonoCore = _configuration["ApiTraspasosCore:UrlNotificacionAbono"];
        }
        public async Task EnviarNotificaciónAbono(NotificacionAbonoTraspasoDto data)
        {

            try
            {
                var token = _jwtIssuerService.GenerateTokenTraspasosCore();
                var httpClient = _httpClientFactory.CreateClient("TraspasosCoreClient");
                var _req = new TraspasosCoreRequest
                {
                    Traspaso = JsonSerializer.Serialize(data)
                };
                var requestMessage = new HttpRequestMessage(HttpMethod.Post, _urlNotificacionAbonoCore)
                {
                    Content = JsonContent.Create(_req)
                };



                requestMessage.Headers.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
                var respuestaJson = await httpClient.SendAsync(requestMessage);

                string contenido = await respuestaJson.Content.ReadAsStringAsync();
                _logger.LogInformation($"Contenido respuesta: {contenido}");
                if (respuestaJson.IsSuccessStatusCode)
                {

                    var httpResponse = JsonSerializer.Deserialize<TraspasosCoreResponse>(contenido);
                    _logger.LogInformation($"Respuesta del servicio Traspasos Core: {JsonSerializer.Serialize(httpResponse)}");
                }
                else
                {
                    _logger.LogInformation($"Error al enviar traspaso: {contenido}");
                    throw new Exception($"Error al enviar el traspaso a {_urlNotificacionAbonoCore}");
                }
            }
            catch (TaskCanceledException ex)
            {
                _logger.LogError($"Ocurrio un error de timeout. ", ex);
                Console.WriteLine($"Error: {ex}");
                throw new Exception("No se pudo validar la firma.");

            }
            catch (Exception ex)
            {
                _logger.LogError($"Ocurrio un error. ", ex);
                Console.WriteLine($"Error: {ex}");
                throw new Exception("No se pudo validar la firma.");
            }
        }
    }
}
