using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaRefResponseDescripcion
    {
        [JsonPropertyName("cuentaReferenciada")]
        public string CuentaReferenciada { get; set; }
        [JsonPropertyName("firma")]
        public string Firma { get; set; }
        [JsonPropertyName("key")]
        public string Key { get; set; }
    }
}
