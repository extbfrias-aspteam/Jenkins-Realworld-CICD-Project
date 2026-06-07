namespace Asp.Api.Azul.Kafka.Dtos.Traspasos
{
    public record TraspasoEiyuToAspDto
    {
        public string CuentaOrdenante { get; set; }
        public string CuentaBeneficiario { get; set; }
        public decimal Monto { get; set; }
        public string ConceptoPago { get; set; }
        public string ClaveRastreo { get; set; }
        public int IdTipoPago { get; set; }
        public string FechaCaptura { get; set; }
        public int IdPblu { get; set; }
        public string AbonoJson { get; set; }
    }
}
