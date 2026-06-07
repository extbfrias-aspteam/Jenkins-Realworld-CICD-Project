using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Business.Pago
{
	public interface IPagoBusiness
	{
        /// <summary>
        /// 
        /// </summary>
        /// <param name="jsonPago"></param>
        /// <param name="idPblu"></param>
        /// <param name="firma"></param>
        /// <param name="estado"></param>
        /// <param name="llave"></param>
        /// <returns>Tuple<string,string> donde Item1: CveRastreo y Item2: JsonPagoAsp</returns>
        /// <exception cref="ErrorPagoExistente"></exception>
        /// <exception cref="ErrorPeticionMalformada"></exception>
        /// <exception cref="ErrorConsultaCuenta"></exception>
        /// <exception cref="ErrorUdnNoExiste"></exception>
        /// <exception cref="ErrorClabeSinUDN"></exception>
        /// <exception cref="ErrorSaldoInsuficiente"></exception>
        /// <exception cref="ErrorClaveRastreoNoExiste"></exception>
        Task<DtoPagoValidado> ValidaPago(OrdenPagoDto ordenPago, string jsonPago, int idPblu, string firma, int estado, int llave, bool isPortal = false, string IDENTIFICADOR = "");

        Task PagoRechazado(string cveRastreo, string descripcion);
		Task<bool> PagoPendiente(string cveRastreo, string descripcion);

		Task<bool> PagoCambioEstado(string cveRastreo, int estado, string causa, DateTime fechaOperacion,
			int idCausaDevolucion);

        Task<bool> MultiplesCambiosDeEstado(List<string> ClavesDeRastreo, int IdEstadoPago);

        Task<DatosCuentaPrevFraudeDto> ObtenerDatosCuenta(string IDENTIFICADOR, string clabe, decimal monto);

        Task ValidarPago(string IDENTIFICADOR, int id_pblu, bool isPortal, OrdenPagoDto ordenPago);

        Task<SpeiOutDto> ProcesarPagoValidado(DtoDatosOriginalesPago datosOriginalesPago);
	}
}
