using Asp.Api.Azul.Models.Entities;
using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Entities.Business
{
    public class KafkaConsumer
    {
        [JsonPropertyName("origen")]
        public int Origen { get; set; }
        [JsonPropertyName("usuario")]
        public string Usuario { get; set; }
        [JsonPropertyName("valorReferencia")]
        public string ValorReferencia { get; set; }
        [JsonPropertyName("fecha_envio")]
        public string Fecha_envio { get; set; } 
    }
}
