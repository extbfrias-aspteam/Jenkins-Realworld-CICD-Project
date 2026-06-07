using Asp.Cifrado.Entities.DataBase;

namespace Asp.Cifrado.Repositorys.ParametrosApiRepository
{
	public interface IParametrosApiRepository
	{
		Task<ParametrosApi> GetById(int idParam);
	}
}
