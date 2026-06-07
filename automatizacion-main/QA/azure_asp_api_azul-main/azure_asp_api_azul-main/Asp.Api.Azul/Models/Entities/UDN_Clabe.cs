using Asp.Api.Azul.Helpers;
using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    [DbTable("udn_clabe")]
    public class UDN_CLABE
    {
        [DbColumn("id_udn")]
        [JsonPropertyName("id_udn")]
        public int Id_udn { get; set; }
        [DbColumn("prefijo_clabe")]
        [JsonPropertyName("prefijo_clabe")]
        public string Prefijo_clabe { get; set; }
        [DbColumn("contador_clabe")]
        [JsonPropertyName("contador_clabe")]
        public int Contador_clabe { get; set; }
    }
}
