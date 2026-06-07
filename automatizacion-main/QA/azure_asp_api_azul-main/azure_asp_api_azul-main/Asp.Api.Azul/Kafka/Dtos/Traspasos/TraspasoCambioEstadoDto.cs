namespace Asp.Api.Azul.Kafka.Dtos.Traspasos
{
    public record TraspasoCambioEstadoDto
    {
        public string ClaveRastreo { get; set; }
        public string Estado { get; set; }
        public string Causa { get; set; }
        public string FechaOperacion { get; set; }
        public int IdPblu { get; set; }
    }
}
