using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Repositorys.AspLogsRepository;
using System.Net.Http.Headers;
using System.Text.Json;

namespace Asp.Api.Azul.Services
{
    public class AspLogService: IAspLogservice
    {
        private readonly IAspLogsRepository _aspLogsRepository;

        public AspLogService(IAspLogsRepository aspLogsRepository)
        {
            _aspLogsRepository = aspLogsRepository;
        }
        public async Task RegistraCatalogoLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu)
        {
            try
            {
                var stringObject = new
                {
                    StatusCode = statusCode,
                    Body = response
                };
                var catalogoLog = new CatalogoLog
                {
                    Fecha = DateTime.Now,
                    EndPoint = endPoint,
                    Request = JsonSerializer.Serialize(request),
                    Response = JsonSerializer.Serialize(stringObject),
                    HttpMethod = httpMethod,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    HeadersResponse = JsonSerializer.Serialize(responseHeader),
                    IdPblu = idPblu
                };

               await _aspLogsRepository.InsertCatalogo(catalogoLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log: " + e.Message);
            }
        }

        public async Task RegistraCuentaLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu)
        {
            try
            {
                var stringObject = new
                {
                    StatusCode = statusCode,
                    Body = response
                };
                var cuentaLog = new CuentaLog
                {
                    Fecha = DateTime.Now,
                    EndPoint = endPoint,
                    Request = JsonSerializer.Serialize(request),
                    Response = JsonSerializer.Serialize(stringObject),
                    HttpMethod = httpMethod,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    HeadersResponse = JsonSerializer.Serialize(responseHeader),
                    IdPblu = idPblu
                };

                await _aspLogsRepository.InsertCuenta(cuentaLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log: " + e.Message);
            }
        }

        public async Task RegistraAspLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu, string clabe)
        {
            try
            {
                var stringObject = new
                {
                    StatusCode = statusCode,
                    Body = response
                };
                var aspLog = new AspLog()
                {
                    Fecha = DateTime.Now,
                    Endpoint = endPoint,
                    Request = JsonSerializer.Serialize(request),
                    Response = JsonSerializer.Serialize(stringObject),
                    HttpMethod = httpMethod,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    HeadersResponse = JsonSerializer.Serialize(responseHeader),
                    IdPblu = idPblu,
                    Clabe = clabe
                };

                await _aspLogsRepository.InsertAsp(aspLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log: " + e.Message);
            }
        }

        public async Task RegistraLoginLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu)
        {
            try
            {
                var stringObject = new
                {
                    StatusCode = statusCode,
                    Body = response
                };
                var loginLog = new LoginLog
                {
                    Fecha = DateTime.Now,
                    EndPoint = endPoint,
                    Request = JsonSerializer.Serialize(request),
                    Response = JsonSerializer.Serialize(stringObject),
                    HttpMethod = httpMethod,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    HeadersResponse = JsonSerializer.Serialize(responseHeader),
                    IdPblu = idPblu
                };

                await _aspLogsRepository.InsertLogin(loginLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log: " + e.Message);
            }
        }

        public async Task RegistraPagoLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu, string claveRastreo)
        {
            try
            {
                var stringObject = new
                {
                    StatusCode = statusCode,
                    Body = response
                };
                var pagoLog = new PagoLog
                {
                    Fecha = DateTime.Now,
                    EndPoint = endPoint,
                    Request = JsonSerializer.Serialize(request),
                    Response = JsonSerializer.Serialize(stringObject),
                    HttpMethod = httpMethod,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    HeadersResponse = JsonSerializer.Serialize(responseHeader),
                    IdPblu = idPblu,
                    ClaveRastreo = claveRastreo
                };

                await _aspLogsRepository.InsertPago(pagoLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log: " + e.Message);
            }
        }
        public async Task RegistraSaldoLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu)
        {
            try
            {
                var stringObject = new
                {
                    StatusCode = statusCode,
                    Body = response
                };
                var saldoLog = new SaldoLog
                {
                    Fecha = DateTime.Now,
                    EndPoint = endPoint,
                    Request = JsonSerializer.Serialize(request),
                    Response = JsonSerializer.Serialize(stringObject),
                    HttpMethod = httpMethod,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    HeadersResponse = JsonSerializer.Serialize(responseHeader),
                    IdPblu = idPblu
                };

                await _aspLogsRepository.InsertSaldo(saldoLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log: " + e.Message);
            }
        }

        public async Task RegistraError(string endPoint, string httpMethod, object request, object requestHeaders, Exception exception, int idPblu, string claveRastreo = "")
        {
            try
            {
                var errorLog = new ErrorLog
                {
                    EndPoint = endPoint,
                    HttpMethod = httpMethod,
                    Request = JsonSerializer.Serialize(request),
                    Method = exception?.TargetSite?.Name ?? string.Empty,
                    Message = exception?.Message ?? string.Empty,
                    StackTrace = exception?.StackTrace ?? string.Empty,
                    Fecha = DateTime.Now,
                    idPblu = idPblu,
                    HeadersRequest = JsonSerializer.Serialize(requestHeaders),
                    ClaveRastreo = claveRastreo
                };
                await _aspLogsRepository.InsertError(errorLog);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error al registrar el log de error: " + e.Message);
            }
        }

    }
    public interface IAspLogservice
    {
        Task RegistraCatalogoLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu);
        Task RegistraCuentaLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu);
        Task RegistraLoginLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu);
        Task RegistraPagoLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu, string claveRastreo);
        Task RegistraSaldoLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu);
        Task RegistraError(string endPoint, string httpMethod, object request, object requestHeaders, Exception exception, int idPblu, string claveRastreo = "");
        Task RegistraAspLog(string endPoint, string httpMethod, object request, string statusCode, object response, object requestHeaders, object responseHeader, int idPblu, string clabe);
    }
}
