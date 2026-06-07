using Asp.Api.Azul.Helpers;
using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    [DbTable("udn")]
    public class UDN
    {

     
        [DbColumn("descripcion")]
        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; }
        [DbColumn("saldo")]
        [JsonPropertyName("saldo")]
        public decimal? Saldo { get; set; }
        [DbColumn("activo")]
        [JsonPropertyName("activo")]
        public bool? Activo { get; set; }
        [DbColumn("fecha_creacion")]
        [JsonPropertyName("fecha_creacion")]
        public DateTime? Fecha_creacion { get; set; }
        [DbColumn("usuario_creacion")]
        [JsonPropertyName("usuario_creacion")]
        public string? Usuario_creacion { get; set; }
        [DbColumn("pblu")]
        [JsonPropertyName("pblu")]
        public int? Pblu { get; set; }
        [DbColumn("saldo_min")]
        [JsonPropertyName("saldo_min")]
        public decimal? Saldo_min { get; set; }
        [DbColumn("notificacion_activa")]
        [JsonPropertyName("notificacion_activa")]
        public bool? Notificacion_activa { get; set; }
        [DbColumn("prefijo_clabe")]
        [JsonPropertyName("prefijo_clabe")]
        public string? Prefijo_clabe { get; set; }
        [DbColumn("contador_clabe")]
        [JsonPropertyName("contador_clabe")]
        public int? Contador_clabe { get; set; }
        [DbColumn("clabe")]
        [JsonPropertyName("clabe")]
        public string? Clabe { get; set; }
        [DbColumn("monto_limite")]
        [JsonPropertyName("monto_limite")]
        public decimal? Monto_limite { get; set; }
    }
}
