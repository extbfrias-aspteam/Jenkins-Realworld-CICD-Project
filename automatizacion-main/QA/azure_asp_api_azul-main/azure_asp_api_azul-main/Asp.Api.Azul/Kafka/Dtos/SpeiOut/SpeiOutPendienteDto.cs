
namespace Asp.Api.Azul.Kafka.Dtos.SpeiOut
{

    public record SpeiOutPendienteDto
    {

        public required string ClaveRastreo { get; set; }
        public required string Descripcion { get; set; }

    }
}
