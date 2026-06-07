using Asp.Api.Azul.Entities.DataBase;
using Asp.Cifrado.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.CatalogoRepository
{
    public interface ICatalogoRepository
    {
        Task<List<DtoBanco>> GetBanco();
     
    }
}
