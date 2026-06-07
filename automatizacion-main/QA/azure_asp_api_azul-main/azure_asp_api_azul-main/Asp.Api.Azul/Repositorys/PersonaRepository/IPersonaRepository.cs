using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.PersonaRepository
{
    public interface IPersonaRepository
    {
        Task<int> Insert(Persona persona);
        Task<Persona?> GetById(int id);
        Task<int> Update(Persona persona);
        Task<int> ExistePersona(string rfc, string curp);
        Task<Persona?> GetByRfc(string rfc);
        Task<Persona?> GetByCurp(string curp);
        Task UpdateCurp(string rfc, string curp);
    }
}
