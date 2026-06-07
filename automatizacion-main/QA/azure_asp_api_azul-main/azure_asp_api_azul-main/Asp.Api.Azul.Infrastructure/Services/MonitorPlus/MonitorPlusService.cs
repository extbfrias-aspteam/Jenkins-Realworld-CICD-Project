using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Infrastructure.Utils;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;

namespace Asp.Api.Azul.Infrastructure.Services.MonitorPlus
{
    public interface IMonitorPlusService
    {
        Task<ApiResponse<bool>> ValidarOperacionMonitorPlus(OrdenPagoDto ordenPago, int IdPblu);
    }
    public class MonitorPlusService : IMonitorPlusService
    {
        private readonly IConfiguration _configuration;
        private readonly IJwtCacheServiceMonitorPlus _jwtCacheServiceMonitorPlus;
        private readonly HttpClient _httpClient;
        private readonly LoggerHelper _logger;
        private readonly ILoggerService _loggerService;

        public MonitorPlusService(
            IConfiguration configuration,
            IJwtCacheServiceMonitorPlus jwtCacheServiceMonitorPlus,
            IHttpClientFactory httpClientFactory,
            LoggerHelper logger,
            ILoggerService loggerService
            )
        {
            _configuration = configuration;
            _jwtCacheServiceMonitorPlus = jwtCacheServiceMonitorPlus;
            _httpClient = httpClientFactory.CreateClient("Contingency");
            _logger = logger;
            _loggerService = loggerService;
        }

        public async Task<ApiResponse<bool>> ValidarOperacionMonitorPlus(OrdenPagoDto ordenPago, int IdPblu)
        {

            string requestJson = "";
            string responseContent = "";
            int statusCode = 0;
            var url = _configuration["MonitorPlus:ValidateUrl"]!;
            try
            {


                var token = await _jwtCacheServiceMonitorPlus.GetAuthTokenMonitorPlusAsync();

              

                var requestDto = BuildRequest(ordenPago, IdPblu.ToString());

                requestJson = JsonSerializer.Serialize(requestDto);
                var content = new StringContent(requestJson, Encoding.UTF8, "application/json");

               using var request = new HttpRequestMessage(HttpMethod.Post, url);
                request.Content = content;
                request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", token);

                var response = await _httpClient.SendAsync(request);
                statusCode = (int)response.StatusCode;

                responseContent = await response.Content.ReadAsStringAsync();

                if (response.StatusCode == System.Net.HttpStatusCode.Unauthorized ||
                    response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                {
                    _logger.LogInformation($"MonitorPlus regresó {response.StatusCode} para clave {ordenPago.CveRastreo}. Se continuará el flujo.");

                    await _loggerService.RegistraLogMonitorPlus(new MonitorPlusLog
                    {
                        Endpoint = url ?? "",
                        HttpMethod = "POST",
                        RequestBody = requestJson,
                        ResponseBody = responseContent,
                        IdPblu = IdPblu,
                        StatusCode = (int)response.StatusCode,
                        HeadersRequest = "Authorization: Bearer",
                        ClaveRastreo = ordenPago.CveRastreo,
                        ErrorMessage = $"MonitorPlus regresó {response.StatusCode} para clave {ordenPago.CveRastreo}. Se continuará el flujo.",
                        StackTrace = "NA",
                        InfoAdicional = "Error en ValidarOperacionMonitorPlus"
                    });

                }

                if (!response.IsSuccessStatusCode)
                {
                    _logger.LogInformation($"Error al validar operación en MonitorPlus. ClaveRastreo:{ordenPago.CveRastreo} Status:{response.StatusCode}");
                    await _loggerService.RegistraLogMonitorPlus(new MonitorPlusLog
                    {
                        Endpoint = url ?? "",
                        HttpMethod = "POST",
                        RequestBody = requestJson,
                        ResponseBody = responseContent,
                        IdPblu = IdPblu,
                        StatusCode = (int)response.StatusCode,
                        HeadersRequest = "Authorization: Bearer",
                        ClaveRastreo = ordenPago.CveRastreo,
                        ErrorMessage = $"Error al validar operación con clave {ordenPago.CveRastreo} en MonitorPlus. HTTP {response.StatusCode} - {responseContent}",
                        StackTrace = "NA",
                        InfoAdicional = "Error en ValidarOperacionMonitorPlus"
                    });


                }

                return new ApiResponse<bool>
                {
                    Success = true,
                    Message = "El flujo continua correctamente.",
                    Data = true
                };
            }
            catch (Exception ex)
            {
                try
                {
                    await _loggerService.RegistraLogMonitorPlus(new MonitorPlusLog
                    {
                        Endpoint = url ?? "",
                        HttpMethod = "POST",
                        RequestBody = JsonSerializer.Serialize(BuildRequest(ordenPago, IdPblu.ToString())),
                        ResponseBody = responseContent,
                        IdPblu = IdPblu,
                        StatusCode = statusCode == 0 ? 500 : statusCode,
                        HeadersRequest = "NA",
                        ClaveRastreo = ordenPago.CveRastreo,
                        ErrorMessage = "Validación MonitorPlus fallida pero se procesara. " + ex.Message,
                        StackTrace = ex.StackTrace ?? "",
                        InfoAdicional = "Error en ValidarOperacionMonitorPlus"
                    });
                }
                catch (Exception e)
                {
                    _logger.LogError("Error al registrar en RegistraLogMonitorPlus.", ex);

                }

                _logger.LogInformation($"Ocurrio un error en ValidarOperacionMonitorPlus, clave de rastreo: {ordenPago.CveRastreo} {ex.Message}:{ex.StackTrace}");

                return new ApiResponse<bool>
                {
                    Success = true,
                    Message = "Validación MonitorPlus fallida pero se procesara.",
                    Data = true
                };
            }
        }
        private string DefaultString() => "X";
        private string DefaultNumber() => "0";

        private MonitorPlusRequestDto BuildRequest(OrdenPagoDto ordenPago, string IdPblu)
        {
            var now = DateTime.Now;

            string DefaultIfEmpty(string? value, string defaultValue = "1")
                => string.IsNullOrWhiteSpace(value) ? defaultValue : value;

            return new MonitorPlusRequestDto
            {
                Type = "WEB",
                PbluId = IdPblu,
                CveRastreo = ordenPago.CveRastreo,
                Amount = decimal.TryParse(ordenPago.Monto, out var amt) ? amt : 1,
                Transaction = new TransactionDto
                {
                    Header = new TransactionHeaderDto
                    {
                        Rtind = "S",
                        Date = now.ToString("ddMMyyyy"),
                        Time = now.ToString("HHmm"),
                        Event = "1348",
                        User = "SERVICIOS",
                        NicknameNode = "ASP",
                        NicknameModule = "DBF"
                    },
                    Fields = new TransactionFieldsDto
                    {
                        CodigoEntidad = "0001",
                        IdCliente = "V23619041",
                        UsuarioBancaDigital = "coaldana",
                        CodigoClienteTitular = "203937",
                        TipoPersona = "N",

                        OrigenDeTransaccion = "APP",
                        AccionDentroSesion = "LOGIN",
                        CodigoTransaccion = "010001",
                        CodigoRespuestaHost = "00",
                        FechaTrx = now.ToString("ddMMyyyy"),
                        HoraTrx = now.ToString("HHmm"),
                        MonedaTrx = "1",
                        MontoTotalTrx = FormatMonto(ordenPago.Monto),
                        MontoTotalTrxUS = "1",
                        TipoCambio = "1",
                        LocalInternacional = "1",
                        TipoTransaccion = "1",
                        NumeroReferencia = "1",
                        ClaveTransferenciaInterbancaria = "1",
                        IDRastreoTransferencias = "1",
                        Reversa = "1",

                        DatosRetoMFA = "1",
                        EnviaPreguntasReto = "1",
                        RespuestasCorrectas = "1",
                        MFARetoSolicitado = "0",
                        MFATipoReto = "1",
                        MFAAprobado = "1",
                        MFAPrioridad = "1",

                        CodigoProductoTitular = "1",
                        CodigoSubproductoTitular = "1",
                        NumerodeCuentaTitular = "1",
                        FechaAperturaCuentaTitular = "1",
                        TipoVIPCuentaTitular = "1",
                        FechaVinculacionCliente = "1",
                        EstatusCuentaProductoTitular = "1",
                        SignoValorSaldoCuentaTitular = "1",
                        SaldoDisponibleCuentaTitular = "1",
                        EmpleadoCuentaTitular = "1",
                        SucursalAperturaCuentaTitular = "1",
                        NombreCompletoClienteCuentaTitular = "1",
                        EjecutivoCuentaTitular = "1",

                        TelefonoLaboralCuentaTitular = "1",
                        TelefonoCelularCuentaTitular = "1",
                        TelefonoResidenciaCuentaTitular = "1",
                        SegmentoClienteCuentaTitular = "1",
                        CorreoElectronicoCuentaTitular = "1",
                        DominioEMailCuentaTitular = "1",

                        FechaAperturaCuentaDestino = "1",
                        EjecutivoCuentaDestino = "1",
                        SucursalAperturaCuentaDestino = "1",
                        TelefonoCuentaDestino = "1",
                        EmailCuentaDestino = "1",
                        DominioEmailDestino = "1",
                        CodigoPaisDestino = "1",
                        CodClienteDestino = "1",
                        EmpleadoCtaDestino = "1",
                        BancoDestino = "1",
                        TipoProductoDestino = "1",
                        NumeroCuentaDestino = "1",
                        NombreClienteTitularDestino = DefaultIfEmpty(ordenPago.NombreDestino, "1"),

                        ProveedorServicio = "1",
                        CategoriaPagoServicio = "1",
                        ReferenciaPagoServicio1 = "1",
                        ReferenciaPagoServicio2 = "1",
                        ReferenciaPagoServicio3 = "1",
                        Origendealerta = "1",
                        IdentTrxFraude = "1",
                        IndicadordeFraude = "1",
                        IndicadorRealTime = "1",
                        IPConexion = "127.0.0.1",
                        Correlativo = "1",
                        SesionID = "1",

                        PaymentSource = "1",
                        Channel = "1",
                        IntegrationType = "1",
                        AuthenticatedService = "1",
                        AuthenticationMethod = "1",
                        ApiKeyPresent = "1",
                        UserInteraction = "1",
                        IdTransaccion = DefaultIfEmpty(ordenPago.CveRastreo, "1"),

                        FingerPrint = new FingerPrintDto()
                    }
                }
            };
        }

        private string FormatMonto(string? monto)
        {
            if (string.IsNullOrWhiteSpace(monto))
            {
                throw new ArgumentNullException(nameof(monto));
            }

            if (decimal.TryParse(monto, System.Globalization.NumberStyles.Any,
                System.Globalization.CultureInfo.InvariantCulture, out decimal resultado))
            {
                return Math.Truncate(resultado * 100).ToString();
            }

            throw new Exception("Error in format Monto");
        }
    }
}
