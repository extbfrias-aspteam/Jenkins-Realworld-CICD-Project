namespace Asp.Api.Azul.Kafka.Dtos.SpeiOut
{
    public record SpeiOutNotificationDto
    {
        public string ClaveRastreo { get; set; }
        public string Estado { get; set; }
        public string Causa { get; set; }
        public DateTime FechaOperacion { get; set; }
        public int IdCausaDevolucion { get; set; }
        public bool Retorno { get; set; }
    }
}
