using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaRefFinalResp
    {
        [JsonPropertyName("CUENTA_REFERENCIA")]
        public string cuenta_referencia { get; set; }
        [JsonPropertyName("OBSERVACIONES")]
        public string observaciones { get; set; }
        [JsonPropertyName("CUENTA_CONCENTRADORA")]
        public string cuenta_concentradora { get; set; }
        [JsonPropertyName("RESULTADO")]
        public string resultado { get; set; }
    }
}
