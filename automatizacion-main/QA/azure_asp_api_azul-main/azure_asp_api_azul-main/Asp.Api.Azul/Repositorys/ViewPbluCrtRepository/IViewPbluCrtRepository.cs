using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.ViewPbluCrtRepository
{
    public interface IViewPbluCrtRepository
    {
        Task<ViewPbluCrt?> GetCertificadoActivo(int idPblue);
    }
}