using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class ConciliacionUdnPblu
    {
        [JsonPropertyName("saldo_actual")]
        public decimal SaldoActual { get; set; }
        [JsonPropertyName("abonos")]
        public SaldoControl Abonos { get; set; }
        [JsonPropertyName("cargos")]
        public SaldoControl Cargos { get; set; }
        [JsonPropertyName("udn_list")]
        public List<UdnSaldo> UdnList { get; set; }
    }
}
