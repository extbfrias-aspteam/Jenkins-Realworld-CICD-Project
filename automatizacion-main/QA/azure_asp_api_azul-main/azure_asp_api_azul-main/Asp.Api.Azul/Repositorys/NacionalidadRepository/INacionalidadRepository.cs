using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.NacionalidadRepository
{
    public interface INacionalidadRepository
    {
        Task<ASPNacionalidad?> findByPaisId(int id);
    }
}
