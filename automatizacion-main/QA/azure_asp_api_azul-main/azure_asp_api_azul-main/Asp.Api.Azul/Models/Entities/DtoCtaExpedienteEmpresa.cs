using System.Text.Json.Serialization;
using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedienteEmpresa
    {
        [JsonPropertyName("razon_social")]
        public string RazonSocial { get; set; }
        [JsonPropertyName("rfc")]
        public string Rfc { get; set; }
        [JsonPropertyName("domicilio")]
        public DtoCtaExpedienteDomicilio Domicilio { get; set; }

        [JsonPropertyName("idIdentificacion")]
       
        public int IdIdentificacion { get; set; }
        [JsonPropertyName("numIdentificacion")]
        public string NumIdentificacion { get; set; }
        [JsonPropertyName("fechaCreacion")]
        public string FechaCreacion { get; set; }
        [JsonPropertyName("entidad")]
        public int Entidad { get; set; }
        [JsonPropertyName("pais")]
        public string Pais { get; set; }
        [JsonPropertyName("nacionalidad")]
        public string Nacionalidad { get; set; }

        [JsonPropertyName("serieFirmaElect")]
       
        public string SerieFirmaElect { get; set; }
        
        [JsonPropertyName("giro")]
        public string Giro { get; set; }
        [JsonPropertyName("telefono")]
        public string Telefono { get; set; }

        [JsonPropertyName("geolocalizacion")]
        
        public string Geolocalizacion { get; set; }
        
        [JsonPropertyName("email")]
        public string Email { get; set; }
        [JsonPropertyName("perfil")]
        public DtoCtaExpedientePerfil Perfil { get; set; }
        [JsonPropertyName("denominacion")]
        public int Denominacion { get; set; }
    }
}
