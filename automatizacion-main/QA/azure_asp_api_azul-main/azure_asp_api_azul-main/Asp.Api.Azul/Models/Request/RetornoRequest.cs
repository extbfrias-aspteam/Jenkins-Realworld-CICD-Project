using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Request
{
    public class RetornoRequest
    {
        [JsonPropertyName("claveRastreo")]
        public required string ClaveRastreo {  get; set; }
        [JsonPropertyName("causaDev")]
        public required int CausaDevolucion { get; set; }
        [JsonPropertyName("uuid")]
        public required string Uuid {  get; set; }

   
      
    }
}
