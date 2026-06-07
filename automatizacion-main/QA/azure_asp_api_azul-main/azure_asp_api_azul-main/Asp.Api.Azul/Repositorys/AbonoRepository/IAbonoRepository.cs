using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Repositorys.AbonoRepository
{
    public interface IAbonoRepository
    {
        Task<bool> ExisteAbono(string cveRastreo, int folioPaquete, int folio, DateTime fechaOperacion);

        Task Insert(Abono abono);

        Task<Abono?> GetAbonoByCveRastreo(string cveRastreo, DateTime fechaOperacion);

        Task<bool> UpdateEstadoRetorno(string cveRastreo, int idEstadoPago, string infoAdicional, DateTime fecha, int idEstadoDevolucion, DateTime fechaOperacion);



        Task<DtoDatosUdnAbono> ObtenerDatosUdn(string cuentaClabe);

        Task<bool> UpdateRetorno(string cveRastreo, int idEstadoPago, string infoAdicional, DateTime fecha,
            int idCausaDevolucion);

    }
}
