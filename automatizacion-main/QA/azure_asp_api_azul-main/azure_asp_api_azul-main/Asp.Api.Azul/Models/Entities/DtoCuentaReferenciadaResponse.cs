using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCuentaReferenciadaResponse
    {
        [JsonPropertyName("estado")]
        public int Estado { get; set; }
        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; }
        [JsonPropertyName("error")]
        public string? Error { get; set; }
    }
}
