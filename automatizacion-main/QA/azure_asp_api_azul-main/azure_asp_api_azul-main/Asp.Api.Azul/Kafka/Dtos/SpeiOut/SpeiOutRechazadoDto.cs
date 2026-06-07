namespace Asp.Api.Azul.Kafka.Dtos.SpeiOut
{
    public record SpeiOutRechazadoDto
    {
        public required string ClaveRastreo { get; set; }
        public required string Descripcion { get; set; }

    }
}
