using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class UdnSaldo
    {
        [JsonPropertyName("id_udn")]
        public int IdUdn { get; set; }
        [JsonPropertyName("udn_descripcion")]
        public string Descripcion { get; set; }
        [JsonPropertyName("saldo_inicial")]
        public decimal SaldoInicial { get; set; }
        [JsonPropertyName("saldo_total")]
        public decimal SaldoTotal { get; set; }
        [JsonPropertyName("abonos")]
        public SaldoControl Abonos { get; set; }
        [JsonPropertyName("cargos")]
        public SaldoControl Cargos { get; set; }
    }
}
