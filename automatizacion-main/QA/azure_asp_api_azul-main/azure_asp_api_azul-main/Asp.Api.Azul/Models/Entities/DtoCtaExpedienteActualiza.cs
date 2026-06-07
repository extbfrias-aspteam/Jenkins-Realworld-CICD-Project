using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedienteActualiza
    {
        [JsonPropertyName("persona")]
        public DtoCtaExpedientePersona persona { get; set; } = new DtoCtaExpedientePersona();


        [JsonPropertyName("domicilio")]
        public DtoCtaExpedienteDomicilio domicilio { get; set; } = new DtoCtaExpedienteDomicilio();


        [JsonPropertyName("comprobantes")]
        public List<DtoCtaExpedienteComprobantes> comprobantes { get; set; } = new List<DtoCtaExpedienteComprobantes>();


        [JsonPropertyName("perfil")]
        public DtoCtaExpedientePerfil perfil { get; set; } = new DtoCtaExpedientePerfil();


        [JsonPropertyName("clabe")]
        public string clabe { get; set; }


        [JsonPropertyName("udnId")]
        public int udnId { get; set; }


        [JsonPropertyName("uuid")]
        public string uuid { get; set; }

        
        [JsonPropertyName("nivel_cuenta")]
        public int nivelCuenta { get; set; }
    }
}
