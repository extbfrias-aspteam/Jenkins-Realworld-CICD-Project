using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.LogsRepository
{
	public interface ILogsRepository
	{
		Task<int> Insert(Logs logs);
        Task InsertRegistroTiempo(RegistroTiemposLogs logs);
    }
}
