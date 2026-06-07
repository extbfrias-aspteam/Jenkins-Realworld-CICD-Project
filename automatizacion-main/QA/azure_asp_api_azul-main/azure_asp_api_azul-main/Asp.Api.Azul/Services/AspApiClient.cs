using System.Text.Json;
using Asp.Api.Azul.Models.Entities;
using Asp.Cifrado.Repositorys.ParametrosApiRepository;
using Asp.Cifrado.Services;
using Microsoft.Extensions.Options;

namespace Asp.Api.Azul.Services
{
    public class AspApiClient : IAspApiClient
    {
        protected readonly IEncriptionService _encriptionService;
        protected readonly HttpClient _apiClient;
        protected readonly IParametrosApiRepository _parametrosApiRepository;
        protected readonly IAspLogservice _aspLogService;
        public AspApiClient(HttpClient httpClient,
            IHttpContextAccessor httpContext,
            IConfiguration configuration,
            IEncriptionService encriptionService,
            IParametrosApiRepository parametrosApiRepository,
            IAspLogservice aspLogService)
        {
            _encriptionService = encriptionService;
            _apiClient = httpClient;
            _parametrosApiRepository = parametrosApiRepository;
            _aspLogService = aspLogService;
        }


        public async Task<List<DtoCtaRefFinalResp>> EnviarAltaRefCtaExpedienteFinal(string jsonEnviar, int idPblu, string clabe, string timestamp = "")
        {
            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", "INICIA Metodo para enviar información al CuentasBLUWS");
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", "Consulta URL CuentasBLUWS");
            }


            
            var parametro = await _parametrosApiRepository.GetById(23);
            var url = parametro.Valor;
            if (string.IsNullOrEmpty(url))
                throw new ErrorGenerico("Url asp mal configurada.");
            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", "INICIA encriptación de los datos");
            }
            var cifrado = _encriptionService.Encript(jsonEnviar, 0);
            _apiClient.DefaultRequestHeaders.Remove("Signature");
            _apiClient.DefaultRequestHeaders.Remove("Key");
            _apiClient.DefaultRequestHeaders.Add("Signature", cifrado.Firma);
            _apiClient.DefaultRequestHeaders.Add("Key", cifrado.Key);

            var cuenta = new
            {
                cuentaReferenciada = cifrado.JsonCifrado
            };
            var requestJson = JsonSerializer.Serialize(cuenta);
            var content = new StringContent(requestJson, System.Text.Encoding.UTF8, "application/json");
            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", $"Se envía la petición POST a {url}");
            }
            var response = await _apiClient.PostAsync(url, content);
            //response.EnsureSuccessStatusCode();
            var stringResponse = await response.Content.ReadAsStringAsync();
            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", $"Respuesta de la petición: {stringResponse}");
            }
            await _aspLogService.RegistraAspLog(url, "POST", requestJson, response.StatusCode.ToString(), stringResponse, new { Signature = cifrado.Firma, Key = cifrado.Key }, new { }, idPblu, clabe);
            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", "Inserta en el log de ASP");
            }

            DtoCuentaReferenciadaResponse? cuentaReferenciadaResponse = JsonSerializer.Deserialize<DtoCuentaReferenciadaResponse>(stringResponse);
            if ((cuentaReferenciadaResponse?.Estado ?? 1) != 0)
            {
                if (timestamp != "")
                {
                    GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", $"Error al crear la cuenta:  {cuentaReferenciadaResponse?.Error ?? "Error al crear cuenta."}");
                }
                throw new ErrorGenerico(cuentaReferenciadaResponse?.Error ?? "Error al crear cuenta.");
            }

            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", "Inicia deserializado y descifrado");
            }
            DtoCtaRefResponseDescripcion result = JsonSerializer.Deserialize<DtoCtaRefResponseDescripcion>(cuentaReferenciadaResponse.Descripcion);

            var aspResult = _encriptionService.Decript(result.CuentaReferenciada, result.Key);
            List<DtoCtaRefFinalResp> listCtas = JsonSerializer.Deserialize<List<DtoCtaRefFinalResp>>(aspResult);
            if (timestamp != "")
            {
                GenerarLog(timestamp, "EnviarAltaRefCtaExpedienteFinal", "Termina el flujo de EnviarAltaRefCtaExpedienteFinal");
            }
            return listCtas;
        }
        private void GenerarLog(string timestamp, string metodo, string text)
        {
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.ffffff");
            Console.WriteLine($"{current_time_formatt} INFO [{metodo} - {timestamp}] -> {text}");

        }
    }
}
