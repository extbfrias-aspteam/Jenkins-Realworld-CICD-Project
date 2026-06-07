using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoAbonoTraspaso
    {

        [JsonPropertyName("rfcDestino")]
        public required string RfcDestino { get; set; }

        [JsonPropertyName("cveRastreo")]
        public required string CveRastreo { get; set; }

        [JsonPropertyName("nombOrigen")]
        public required string NombOrigen { get; set; }

        [JsonPropertyName("refCob")]
        public required string RefCob { get; set; }

        [JsonPropertyName("idTipoPago")]
        public required string IdTipoPago { get; set; }

        [JsonPropertyName("conceptoPago")]
        public required string ConceptoPago { get; set; }

        [JsonPropertyName("causaDev")]
        public required string CausaDev { get; set; }

        [JsonPropertyName("folio_paquete")]
        public required string Folio_paquete { get; set; }

        [JsonPropertyName("fhOperacion")]
        public required string FhOperacion { get; set; }

        [JsonPropertyName("idTipoCtaDestino")]
        public required string IdTipoCtaDestino { get; set; }

        [JsonPropertyName("nombreDestino")]
        public required string NombreDestino { get; set; }

        [JsonPropertyName("bancoOrigen")]
        public required string BancoOrigen { get; set; }

        [JsonPropertyName("monto")]
        public required string Monto { get; set; }

        [JsonPropertyName("refNum")]
        public required string RefNum { get; set; }

        [JsonPropertyName("iva")]
        public required string Iva { get; set; }

        [JsonPropertyName("cuentaReferencia")]
        public required string CuentaReferencia { get; set; }

        [JsonPropertyName("folio")]
        public required string Folio { get; set; }

        [JsonPropertyName("rfcOrigen")]
        public required string RfcOrigen { get; set; }

        [JsonPropertyName("cuentaConcentradora")]
        public required string CuentaConcentradora { get; set; }

        [JsonPropertyName("clabe")]
        public required string Clabe { get; set; }

        [JsonPropertyName("ctaDestino")]
        public required string CuentaDestino { get; set; }

        [JsonPropertyName("fecha_captura")]
        public required string FechaCaptura { get; set; }
        [JsonPropertyName("uuid")]
        public required string Uuid { get; set; }



    }
}
