using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.ViewConciliacionHistRepository
{
	public interface IViewConciliacionHistRepository
	{
		Task<List<ViewConciliacionHist>> GetBy(int idPblu);
		Task<ViewConciliacionHist?> GetBy(int idPblu, int idUdn);
		Task<ViewConciliacionHist?> GetByUdnFecha(int idUdn, DateTime fecha);
        Task<string?> GetBy(string clabe);

        Task<string?> GetByUdn(string udn);
    }
}
