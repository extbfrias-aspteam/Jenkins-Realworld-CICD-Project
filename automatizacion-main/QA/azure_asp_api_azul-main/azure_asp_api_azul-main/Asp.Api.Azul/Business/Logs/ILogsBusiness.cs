using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Business.Logs
{
    public interface ILogsBusiness
    {
        Task<Entities.DataBase.Logs> RegistraErrorAzul(ErrorGenerico error, int idPblu, LogLevel logLevel,
            string application);

        Task<Entities.DataBase.Logs> RegistraError(Exception error, int idPblu, LogLevel logLevel, string application);

        Task RegistraTiempos(string cve_rastreo, string idPblu, string peticion, string tiempo_llegada, string tiempo_respuesta, string respuesta);
    }
}
