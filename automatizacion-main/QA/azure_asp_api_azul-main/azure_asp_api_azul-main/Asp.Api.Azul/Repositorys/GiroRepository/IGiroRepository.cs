using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.GiroRepository
{
    public interface IGiroRepository
    {
        Task<ASPGiro?> GetById(int giroId);
    }
}
