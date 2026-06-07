using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.PbluRepository
{
	public interface IPbluRepository
	{
		Task<List<Pblu>> GetAll();
        Task<Pblu?> GetById(int idPblu);
        Task<string?> ObtenerBloqueoAltaReferencia(int idPblu);

    }
}
