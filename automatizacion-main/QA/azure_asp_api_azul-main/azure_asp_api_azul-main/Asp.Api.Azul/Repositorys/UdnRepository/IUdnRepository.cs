using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Utilities;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Repositorys.UdnRepository
{
    public interface IUdnRepository
    {
        Task<Udn?> GetById(int idUdn);
        Task<Udn?> GetByClabe(string clabe);
        Task<List<Udn>> GetByIdPblu(int idPblu);
		//void UpdateSaldo(int idUdn, decimal nuevoSaldo);
        Task AumentaSaldo(string claveRastreo,int idUdn, decimal monto);
        Task<bool> DisminuyeSaldo(int idUdn, decimal monto);


        Task<bool> UpdateClabe(int idUdn, string clabe);

        Task<string> GetClabe(int idUdn);
        Task<Dictionary<string, object>> InsertarUDN(UDN udn, int IdPblue, string Username);

        Task<int> ExisteDescripcionUdn(string descripcion,int idpblu);

        Task<bool> VerificaUdnByIdPblu(int idPblu,int id_udn);
    }
}
