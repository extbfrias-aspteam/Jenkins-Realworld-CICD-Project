using Asp.Api.Azul.Entities.DataBase;

namespace Asp.Api.Azul.Repositorys.PagoRepository
{
    public interface IPagoRepository
    {


        Task<bool> UpdateRechazado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu,
            string infoAdicional, DateTime fechaRechazo);

        Task<bool> UpdateAceptado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu,
            string infoAdicional, DateTime fechaAceptado);

        Task<bool> UpdateCancelado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu,
            string infoAdicional, DateTime fechaCancelado);

        Task<bool> UpdateLiquidado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu,
            string infoAdicional, DateTime fechaLiquidado);

        Task<Pago?> GetPagoByCveRastreo(string cveRastreo);



        Task<ValidaPago> GetValidacionPago(int idPblu, string cveRastreo, int bancoDestino, string clabeOrigen, bool isPortal, decimal monto, string ctaDestino);

        Task<int> InsertPagoPl(Pago pago, int folioPaquete);

        Task<bool> InsertMultiplesEstados(List<string> ClavesDeRastreo, int IdEstadoPago);

        Task<InsertTraspasoResponse?> InsertTraspaso(Pago pago, string nombreOrdenante, string UuidAbono);

        Task<DatosCuentaPrevFraudeDto?> ObtenerDatosCuenta(string clabe);
    }
}
