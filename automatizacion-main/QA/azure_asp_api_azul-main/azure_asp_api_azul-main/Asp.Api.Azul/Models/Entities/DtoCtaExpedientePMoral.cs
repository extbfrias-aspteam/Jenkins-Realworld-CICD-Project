using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedientePMoral
    {
        [JsonPropertyName("repLegal")]
        public DtoCtaExpedienteRepLegal RepresentanteLegal { get; set; }
        [JsonPropertyName("personaMoral")]
        public DtoCtaExpedienteEmpresa PersonaMoral { get; set; }
        [JsonPropertyName("udnId")]
        public int UdnId { get; set; }
        [JsonPropertyName("uuid")]
        public string Uuid { get; set; }
        [JsonPropertyName("nivel_cuenta")]
        public int NivelCuenta { get; set; }
    }
}
