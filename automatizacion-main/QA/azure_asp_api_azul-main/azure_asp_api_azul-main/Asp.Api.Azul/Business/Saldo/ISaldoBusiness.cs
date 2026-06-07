using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Business.Saldo
{
	public interface ISaldoBusiness
	{
		Task<ConciliacionUdnPblu> GetSaldoDiaActual(int idPblu);
		Task<DtoSaldoUdn> GetSaldoByClabe(int idPblu, string clabe);
		Task InicioDia();
		Task CierreDia(bool anterior = false);

        Task InicioDia(DateTime fecha);
        Task CierreDia(DateTime fecha);
        Task<string> GetSaldoUdnByClabe(string clabe);
        Task<string> GetSaldoUdnByUdnEs(string udn);
		Task<bool> VerificarUdnByPblu(int id_pblu,int id_udn);
    }
}
