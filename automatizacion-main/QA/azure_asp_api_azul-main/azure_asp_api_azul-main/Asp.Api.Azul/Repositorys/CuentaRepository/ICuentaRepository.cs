using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.CuentaRepository
{
    public interface ICuentaRepository
    {
        Task<Cuenta?> GetByClabe(string clabe);
        Task<bool> Existe(string clabe);
        Task<string> GenerarCuentaClabe(int idPblu, int idUdn);       
        Task<int> Insert(Cuenta cuenta);
        Task<bool> ActualizaEstado(Cuenta cuenta);
        Task<bool> VerificarCuentaByPblu(int idPblu,string clabe);
        Task<bool> ActivarCuentaClabe(string clabe);
        bool GetCamposObligatorios();
        Task<bool> IsActiveDigitalizacionPblu(int id_pblu);
    }
}