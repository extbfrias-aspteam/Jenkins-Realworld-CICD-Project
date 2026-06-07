using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.AccessRepository
{
    public interface IAccessRepository
    {
        Task<Access?> FindByUser(string paeamString);
    }
}