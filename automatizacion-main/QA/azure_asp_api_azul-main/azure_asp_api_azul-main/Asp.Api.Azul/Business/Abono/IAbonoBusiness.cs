using Asp.Api.Azul.Entities.Business;

namespace Asp.Api.Azul.Business.Abono
{
	public interface IAbonoBusiness
	{
		Task RecibeAbono(OrdenAbono ordenAbono, string firma, bool validarInexistencia = true);

        Task<bool> CambioEstadoRetorno(string cveRastreo, int estado, string causa, DateTime fecha,
            int idCausaDevolucion, string fechaOperacion);

        Task<bool> RecibeRetorno(OrdenAbono ordenAbono, string firma);


    }
}
