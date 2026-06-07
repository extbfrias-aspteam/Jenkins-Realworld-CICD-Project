using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.HistorialSaldoPbluRepository
{
	public interface IHistorialSaldoPbluRepository
	{
		Task<HistorialSaldoPblu?> GetByIdUdnAndLastFechaOperativa(int idUdn);
		Task Insert(HistorialSaldoPblu historialSaldoPblu);

		Task<bool> ExisteByIdUdnAndFechaOperativa(int idUdn, DateTime fechaOperativa);
		Task<bool> UpdateSaldoFinal(int idUdn, DateTime fechaOperativa, decimal saldoFinal);
	}
}
