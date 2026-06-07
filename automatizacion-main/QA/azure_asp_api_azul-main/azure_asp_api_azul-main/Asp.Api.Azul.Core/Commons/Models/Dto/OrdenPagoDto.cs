using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Core.Commons.Models.Dto
{
    public class OrdenPagoDto
    {
        [JsonPropertyName("bancoDestino")]
        public string BancoDestino { get; set; }
        [JsonPropertyName("clabe")]
        public string Clabe { get; set; }
        [JsonPropertyName("nombreDestino")]
        public string NombreDestino { get; set; }
        [JsonPropertyName("idTipoCtaDestino")]
        public string IdTipoCtaDestino { get; set; }
        [JsonPropertyName("ctaDestino")]
        public string CtaDestino { get; set; }
        [JsonPropertyName("rfcDestino")]
        public string RfcDestino { get; set; }
        [JsonPropertyName("conceptoPago")]
        public string ConceptoPago { get; set; }
        [JsonPropertyName("monto")]
        public string Monto { get; set; }
        [JsonPropertyName("iva")]
        public string Iva { get; set; }
        [JsonPropertyName("refNum")]
        public string RefNum { get; set; }
        [JsonPropertyName("refCob")]
        public string RefCob { get; set; }
        [JsonPropertyName("timeStamp")]
        public string TimeStamp { get; set; }
        [JsonPropertyName("otp")]
        public string Otp { get; set; }
        [JsonPropertyName("uuid")]
        public string Uuid { get; set; }
        [JsonPropertyName("cveRastreo")]
        public string CveRastreo { get; set; }
    }
}