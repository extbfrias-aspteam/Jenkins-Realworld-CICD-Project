namespace Asp.Api.Azul.Models.Entities
{
    public class DtoTraspasoAsp
    {
        public string CuentaOrdenante { get; set; }
        public string CuentaBeneficiario { get; set; }
        public decimal Monto { get; set; }
        public string ConceptoPago { get; set; }
        public string ClaveRastreo { get; set; }
        public int IdTipoPago { get; set; }
        public string FechaCaptura { get; set; }
        public string AbonoJson { get; set; }
    }
}
