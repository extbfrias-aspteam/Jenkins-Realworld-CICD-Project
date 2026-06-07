using System.Security.Cryptography;
using System.Text;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Repositorys.LogsRepository;

namespace Asp.Api.Azul.Business.Logs
{
	public class LogBusiness : ILogsBusiness
	{
		private readonly ILogsRepository _logsRepository;

		public LogBusiness(ILogsRepository logsRepository)
		{
			_logsRepository = logsRepository;
		}

		public async Task<Entities.DataBase.Logs> RegistraErrorAzul(ErrorGenerico error, int idPblu, LogLevel logLevel,
			string application)
		{
			Entities.DataBase.Logs log = new Entities.DataBase.Logs();
			log.Application = application;
			log.FechaLog = DateTime.Now;
			log.IdError = error.IdError;
			log.LogLevel = logLevel.ToString();
			if (error.CveRastreo == "default")
			{
				log.CveRastreo = GeneraCveRastreo();
			}
			else
			{
				log.CveRastreo = error.CveRastreo;
			}

			log.IdPblu = idPblu;
			if (error.IdError == -1 && idPblu > 0)
			{
                log.Fuente = "No se encontró cierre de fecha operativa : " + DateTime.Now.AddDays(-1).ToString("yyyy-MM-dd");
            }
			else
			{
                log.Fuente = ErrorTrace.GetStackTrunkedTrace(error);
            }
			log.IdLog = await _logsRepository.Insert(log);
			return log;
		}

		public async Task<Entities.DataBase.Logs> RegistraError(Exception error, int idPblu, LogLevel logLevel, string application)
		{
			Entities.DataBase.Logs log = new Entities.DataBase.Logs();
			log.Application = application;
			log.FechaLog = DateTime.Now;
			log.IdError = -1;
			log.LogLevel = logLevel.ToString();
			log.CveRastreo = "indefinida";
			log.IdPblu = idPblu;
			log.Fuente = ErrorTrace.GetStackTrunkedTrace(error);
			log.IdLog = await _logsRepository.Insert(log);
			return log;
		}
        public async Task RegistraTiempos(string cve_rastreo, string idPblu, string peticion, string tiempo_llegada, string tiempo_respuesta, string respuesta)
        {
            Entities.DataBase.RegistroTiemposLogs log = new Entities.DataBase.RegistroTiemposLogs();
            log.cve_rastreo = cve_rastreo;
            log.idPblu = idPblu.ToString();
            log.peticion = peticion;
            log.tiempo_llegada = tiempo_llegada;
            log.tiempo_respuesta = tiempo_respuesta;
            log.respuesta = respuesta;
			try
			{
                await _logsRepository.InsertRegistroTiempo(log);
            }catch(Exception ex)
			{
                Console.WriteLine(ex.Message);
                Console.WriteLine(ex.StackTrace);
            }

        }
        public string GeneraCveRastreo()
		{
			StringBuilder cve = new StringBuilder(DateTime.Now.ToString("yyyyMMDD-HHmmsssss-"));
			try
			{
				int aleatorio = RandomNumberGenerator.GetInt32(10000);
				cve.Append(aleatorio);
			}
			catch (Exception e)
			{
				// clave de rastreo generada solo con fecha
			}

			return cve.ToString();
		}
	}
}
