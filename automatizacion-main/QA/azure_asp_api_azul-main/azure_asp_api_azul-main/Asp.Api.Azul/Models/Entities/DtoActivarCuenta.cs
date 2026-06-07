using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoActivarCuenta
    {
        [JsonPropertyName("clabe")]
        public string Clabe { get; set; }
    }
}
