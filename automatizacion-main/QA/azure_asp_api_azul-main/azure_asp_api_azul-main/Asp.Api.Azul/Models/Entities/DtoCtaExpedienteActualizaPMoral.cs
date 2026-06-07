using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedienteActualizaPMoral: DtoCtaExpedientePMoral
    {
        [JsonPropertyName("clabe")]
        public string clabe { get; set; }
    }
}
