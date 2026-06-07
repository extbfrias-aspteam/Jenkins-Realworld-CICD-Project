using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;

namespace Asp.Api.Azul.Business.Retornos
{
    public interface IRetornoBusiness
    {
        Task InsertarRetornoAsync(RetornoRequest request, int id_pblu,string IDENTIFICADOR);
    }
}
