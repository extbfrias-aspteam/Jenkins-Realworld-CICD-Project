using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.ViewPbluUdnRepository
{
    public interface IViewPbluUdnRepository
    {
        Task<ViewPbluUdn?> GetUdnById(int id);
    }
}