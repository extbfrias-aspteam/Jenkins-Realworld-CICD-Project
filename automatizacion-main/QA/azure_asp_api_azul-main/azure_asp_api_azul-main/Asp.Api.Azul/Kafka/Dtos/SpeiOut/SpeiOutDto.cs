namespace Asp.Api.Azul.Kafka.Dtos.SpeiOut
{
    public record SpeiOutDto
    {
        public int IdPblu { get; set; }
        public string ClaveRastreo { get; set; }
        public string Pago { get; set; }
        public string Key { get; set; }
        public string Signature { get; set; }
        public string Proveedor { get; set; }
    }
}
