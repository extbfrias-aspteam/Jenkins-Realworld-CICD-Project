using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.OcupacionRepository
{
    public interface IOcupacionRepository
    {
        Task<AspOcupacion?> getOptionalOcupacionById(int ocuId);
    }
}