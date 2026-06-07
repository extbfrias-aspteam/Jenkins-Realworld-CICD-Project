using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.ExpedienteRepository
{
    public interface IExpedienteRepository
    {
        Task<int> Insert(Expediente expediente);
        Task<bool> UpdateClabe(int idExpediente, string clabe);
    }
}
