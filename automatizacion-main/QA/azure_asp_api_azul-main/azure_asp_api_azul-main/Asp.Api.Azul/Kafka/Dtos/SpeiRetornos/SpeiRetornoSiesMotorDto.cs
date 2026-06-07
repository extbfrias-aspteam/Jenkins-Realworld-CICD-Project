namespace Asp.Api.Azul.Kafka.Dtos.SpeiRetornos
{
    public record SpeiRetornoSiesMotorDto
    {


        public string CuentaOrdenante { get; set; }
        public string CuentaBeneficiario { get; set; }
        public string Monto { get; set; }
        public string ClaveRastreo { get; set; }
        public string DevolucionId { get; set; }
        public string ConceptoPago { get; set; }
        public string NombreOrdenante { get; set; }
        public string NombreBeneficiario { get; set; }
        public string FechaOperacion { get; set; }
        public string Folio { get; set; }
        public string FolioPaquete { get; set; }
        public int IdPblu { get; set; }
        public string BancoOrigen { get; set; }
        public string ConceptoOriginal { get; set; }
        public string RefNum { get; set; }
        public bool Bloqueado { get; set; }


    }
}
