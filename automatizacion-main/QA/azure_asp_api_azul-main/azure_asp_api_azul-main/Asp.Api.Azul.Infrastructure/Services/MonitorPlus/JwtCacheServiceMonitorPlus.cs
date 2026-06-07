using Asp.Api.Azul.Core.Commons.Constants;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Infrastructure.Utils;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Reflection.Metadata;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Infrastructure.Services.MonitorPlus
{
    public interface IJwtCacheServiceMonitorPlus
    {
        Task<string> GetAuthTokenMonitorPlusAsync();
    }
    public class JwtCacheServiceMonitorPlus: IJwtCacheServiceMonitorPlus
    {
        private readonly IConfiguration _configuration;

        private DateTime _accessTokenExpiration = DateTime.MinValue;
        private string? _accessToken;
        private readonly SemaphoreSlim _lock = new(1, 1);
        private readonly LoggerHelper _logger;
        private readonly HttpClient _httpClient;
        private readonly IConfigurationService _configurationService;
        private readonly ILoggerService _loggerService;

        public JwtCacheServiceMonitorPlus(IConfiguration configuration, LoggerHelper logger, IHttpClientFactory httpClientFactory, IConfigurationService configurationService, ILoggerService loggerService)
        {
            _configuration = configuration;
            _logger= logger;
            _httpClient = httpClientFactory.CreateClient("Contingency");
            _httpClient.Timeout = TimeSpan.FromSeconds(10);
            _configurationService = configurationService;
            _loggerService = loggerService;
        }
 
        public async Task<string> GetAuthTokenMonitorPlusAsync()
        {
            try
            {

                if (!string.IsNullOrEmpty(_accessToken) && DateTime.UtcNow < _accessTokenExpiration)
                    return _accessToken;

                await _lock.WaitAsync();
                try
                {

                    if (!string.IsNullOrEmpty(_accessToken) && DateTime.UtcNow < _accessTokenExpiration)
                        return _accessToken;

                    _logger.LogInformation("Token JWT expirado o próximo a expirar. Ejecutando login al Monitor Plus.");
                    await LoginAsync(); // obtiene un nuevo token 

                    return _accessToken!;
                }
                finally
                {
                    _lock.Release();
                }
            }
            catch (Exception ex)
            {
                _logger.LogError("Error al obtener o refrescar token JWT de Monitor Plus.",ex);
                throw;
            }
        }
        private async Task LoginAsync()
        {
            var loginUrl = _configuration["MonitorPlus:AuthUrl"];

            string requestJson = "";
            string responseJson = "";
            int statusCode = 0;

            try
            {
                var request = new
                {
                    username = _configuration["MonitorPlus:Username"],
                    password = _configuration["MonitorPlus:Password"]
                };

                requestJson = JsonSerializer.Serialize(request);

                var contentRequest = new StringContent(
                    requestJson,
                    Encoding.UTF8,
                    "application/json"
                );

                var response = await _httpClient.PostAsync(loginUrl, contentRequest);

                statusCode = (int)response.StatusCode;

                responseJson = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                {
                    _logger.LogInformation($"Error HTTP {response.StatusCode} al hacer login en MonitorPlus. Respuesta: {responseJson}");
                    response.EnsureSuccessStatusCode();
                }

                var responseData = JsonSerializer.Deserialize<ApiAuthMonitorPlusResponseDto>(responseJson);

                _accessToken = responseData!.Data.Token;

                int expirationHours = await _configurationService.GetWithCache<int>(
                    GeneralConstants.MonitorPlus.REFRESH_TOKEN_EXPIRATION_HOURS,
                    TimeSpan.FromMinutes(5)
                );

                _accessTokenExpiration = DateTime.UtcNow.AddHours(expirationHours).AddMinutes(-5);

                _logger.LogInformation($"Nuevo token obtenido. Expira a las {_accessTokenExpiration:O}");
            }
            catch (Exception ex)
            {
                var uuid = Guid.NewGuid().ToString();
                try
                {
                    var log = new MonitorPlusLog
                    {
                        Endpoint = loginUrl ?? "",
                        HttpMethod = "POST",
                        RequestBody = "NA",
                        ResponseBody = responseJson,
                        IdPblu = -1,
                        StatusCode = statusCode == 0 ? 500 : statusCode,
                        HeadersRequest = "Content-Type: application/json",
                        ClaveRastreo = uuid,
                        ErrorMessage = ex.Message,
                        StackTrace = ex.StackTrace ?? "",
                        InfoAdicional = $"Metodo:LoginAsync | TokenExpirationActual:{_accessTokenExpiration:O}"
                    };

                    await _loggerService.RegistraLogMonitorPlus(log);
                }
                catch(Exception e)
                {
                    _logger.LogError("Error al registrar en RegistraLogMonitorPlus.", ex);
                  
                }

                _logger.LogError($"Error al obtener o refrescar token JWT de Monitor Plus. {uuid}", ex);
                throw;
            }
        }
    }
}
