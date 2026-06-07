using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoAspCtaExpediente
    {
        [JsonPropertyName("control")]
        public String control { get; set; }

        [JsonPropertyName("consecutivo")]
        public String consecutivo { get; set; }

        [JsonPropertyName("fecha")]
        public String fecha { get; set; }

        [JsonPropertyName("tipoCuenta")]
        public String tipoCuenta { get; set; }

        [JsonPropertyName("accion")]
        public String accion { get; set; }

        [JsonPropertyName("procesado")]
        public String procesado { get; set; }

        [JsonPropertyName("error")]
        public String error { get; set; }

        [JsonPropertyName("observaciones")]
        public String observaciones { get; set; }

        [JsonPropertyName("cuenta_concentradora")]
        public String cuentaConcentradora { get; set; }

        [JsonPropertyName("cuenta_referencia")]
        public String cuentaReferencia { get; set; }

        [JsonPropertyName("nombre_referencia")]
        public String nombreReferencia { get; set; }

        [JsonPropertyName("rfc_referencia")]
        public String rfcReferencia { get; set; }

        [JsonPropertyName("curp_referencia")]
        public String curpReferencia { get; set; }

        [JsonPropertyName("correo_referencia")]
        public String correoReferencia { get; set; }

        [JsonPropertyName("telefono_referencia")]
        public String telefonoReferencia { get; set; }

        [JsonPropertyName("tipo_producto")]
        public String tipoProducto { get; set; } = "01";

        [JsonPropertyName("solicitante")]
        public DtoAspSolicitante solicitante { get; set; }

        [JsonPropertyName("repLegal")]
        public DtoAspRepLegal repLegal { get; set; }
    }
}
