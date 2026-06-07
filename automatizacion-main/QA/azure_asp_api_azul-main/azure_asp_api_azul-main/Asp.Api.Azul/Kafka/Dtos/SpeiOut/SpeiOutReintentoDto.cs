namespace Asp.Api.Azul.Kafka.Dtos.SpeiOut
{
    public record SpeiOutReintentoDto
    {
        public required List<string> ListaClavesDeRastreo { get; init; }
        public required int IdEstadoPago { get; init; }
    }
}
