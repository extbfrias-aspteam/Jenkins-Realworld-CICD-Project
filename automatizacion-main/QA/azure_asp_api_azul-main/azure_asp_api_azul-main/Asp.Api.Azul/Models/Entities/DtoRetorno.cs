namespace Asp.Api.Azul.Models.Entities
{
    public class DtoRetorno
    {
        public required string CuentaOrdenante { get; set; }
        public required string CuentaBeneficiario { get; set; }
        public required string Monto { get; set; }
        public required string ClaveRastreo { get; set; }
        public required string DevolucionId { get; set; }
        public required string ConceptoPago { get; set; }
        public required string NombreOrdenante { get; set; }
        public required string NombreBeneficiario { get; set; }
        public required string FechaOperacion { get; set; }
        public required string Folio { get; set; }
        public required string FolioPaquete { get; set; }
        public required int IdPblu { get; set; }
        public required string BancoOrigen { get; set; }
        public required string ConceptoOriginal { get; set; }
        public required string RefNum { get; set; }
        public required int IdTipoPago { get; set; }
        public required int IdEstadoPago { get; set; }
        public required string IdRetiro { get; set; }
    }
}
