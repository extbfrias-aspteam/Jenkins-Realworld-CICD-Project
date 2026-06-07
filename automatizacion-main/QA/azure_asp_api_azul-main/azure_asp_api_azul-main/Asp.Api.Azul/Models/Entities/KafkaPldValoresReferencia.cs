using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class KafkaPldValoresReferencia
    {
        [JsonPropertyName("monto")]
        public decimal Monto { get; set; }

        [JsonPropertyName("claveRastreo")]
        public string ClaveRastreo { get; set; }

        [JsonPropertyName("cuentaReferencia")]
        public string CuentaReferencia { get; set; }

        [JsonPropertyName("idParticipante")]
        public int IdParticipante { get; set; }

        [JsonPropertyName("fechaTransaccion")]
        public string FechaTransaccion { get; set; }

        [JsonPropertyName("fechaOperacion")]
        public string FechaOperacion { get; set; }

        [JsonPropertyName("categoria")]
        public int Categoria { get; set; }

        [JsonPropertyName("sucursal")]
        public string Sucursal { get; set; }
    }
}
