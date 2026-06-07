using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedienteRepLegal
    {
        [JsonPropertyName("persona")]
        public DtoCtaExpedientePersona Persona { get; set; }
        [JsonPropertyName("domicilio")]
        public DtoCtaExpedienteDomicilio Domicilio { get; set; }
        [JsonPropertyName("comprobantes")]
        public List<DtoCtaExpedienteComprobantes> Comprobantes { get; set; } = new List<DtoCtaExpedienteComprobantes>();

    }
}
