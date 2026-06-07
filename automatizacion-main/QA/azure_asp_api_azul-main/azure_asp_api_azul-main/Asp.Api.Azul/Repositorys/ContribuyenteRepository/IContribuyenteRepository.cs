using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.ContribuyenteRepository
{
    public interface IContribuyenteRepository
    {
  
        Task<int> Insert(Contribuyente contribuyente);
        Task<Contribuyente?> GetByPMoral(int idPersonaPersonaMoral);
        Task<bool> Update(Contribuyente contribuyente);
    }
}
