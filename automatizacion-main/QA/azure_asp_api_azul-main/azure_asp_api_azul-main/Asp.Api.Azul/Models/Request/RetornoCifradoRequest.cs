using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Request
{
    public class RetornoCifradoRequest
    {
        [JsonPropertyName("retorno")]
        public required string Retorno {  get; set; }
    }
}
