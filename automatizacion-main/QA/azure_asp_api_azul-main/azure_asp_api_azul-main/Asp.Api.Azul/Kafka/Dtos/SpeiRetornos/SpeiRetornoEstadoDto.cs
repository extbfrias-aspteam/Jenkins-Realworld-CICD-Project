namespace Asp.Api.Azul.Kafka.Dtos.SpeiRetornos
{
    public record SpeiRetornoEstadoDto
    {
        public string ClaveRastreo { get; set; }
        public int Estado { get; set; }
        public int DevolucionId { get; set; }
        public string FechaOperacion { get; set; }
    }
}
