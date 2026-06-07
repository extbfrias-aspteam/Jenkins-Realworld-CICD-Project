using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Repositorys.RetornoRepository
{
    public interface IRetornoRepository
    {

        Task<DtoRetorno> ObtenerDatosRetorno(string claveRastreo, int id_pblu,string uuid);
    }
}
