namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCambioEstado
    {
        public required string ClaveRastreo { get; set; }

        public required string Estado { get; set; }

        public required string Causa { get; set; }

        public required string Uuid { get; set; }
        public required int IdPblu { get; set; }
    }
}
