using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Business.Cuenta
{
    public interface ICuentaBusiness
    {
        Task<DtoClabe> CrearCuentaPersonaFisica(DtoCtaExpediente cuentaObj, int idPblu, string nombreUsuario);

        Task<DtoClabe> CrearCuentaPersonaMoral(DtoCtaExpedientePMoral cuentaPm, int idPblu, string nombreUsuario, string timestamp);
        Task<DtoClabe> ActualizaCuentaExpediente(DtoCtaExpedienteActualiza ctaEje, int idPblu, string nombreUsuario);
        Task<DtoClabe> ActualizaCuentaExpedienteMoral(DtoCtaExpedienteActualizaPMoral cuentaPm, int idPblu, string nombreUsuario);

        Task<bool> VerificaClabeByIdPblu(int idPblu,string clabe);

        Task<bool> ActivateCuenta(string clabe, int idPblu);

        Task<bool> ExisteActivateCuenta(string clabe);

    }
}
