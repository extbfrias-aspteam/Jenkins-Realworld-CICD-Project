using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.PaisRepository
{
    public interface IPaisRepository
    {
        Task<ASPPais?> GetById(int paisId);
    }
}
