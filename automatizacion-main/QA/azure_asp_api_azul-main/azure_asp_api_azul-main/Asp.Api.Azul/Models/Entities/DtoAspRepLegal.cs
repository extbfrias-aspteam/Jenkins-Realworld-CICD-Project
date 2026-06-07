using System.Text.Json.Serialization;
using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoAspRepLegal
    {
        [JsonPropertyName("nombre_cuenta")]
        [CuentaValidation(obligatorioRl:true)]
        public String nombreCuenta { get; set; }

        [JsonPropertyName("tipo_persona_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String tipoPersonaCuenta { get; set; }

        [JsonPropertyName("pr_apellido_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String prApellidoCuenta { get; set; }

        [JsonPropertyName("sg_apellido_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String sgApellidoCuenta { get; set; }

        [JsonPropertyName("rfc_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String rfcCuenta { get; set; }

        [JsonPropertyName("curp_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String curpCuenta { get; set; }

        [JsonPropertyName("calle_principal_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String callePrincipalCuenta { get; set; }

        [JsonPropertyName("calle_secundaria_cuenta")]
        public String calleSecundariaCuenta { get; set; }

        [JsonPropertyName("calle_secundaria2_cuenta")]
        public String calleSecundaria2Cuenta { get; set; }

        [JsonPropertyName("no_interior_cuenta")]
        public String noInteriorCuenta { get; set; }

        [JsonPropertyName("no_exterior_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String noExteriorCuenta { get; set; }

        [JsonPropertyName("coloniaId_cuenta")]
        public String coloniaIdCuenta { get; set; }

        [JsonPropertyName("ciudad_cuenta")]
        public String ciudadCuenta { get; set; }

        [JsonPropertyName("celular_cuenta")]
        public String celularCuenta { get; set; }

        [JsonPropertyName("correo_cuenta")]
        public String correoCuenta { get; set; }

        [JsonPropertyName("genero_cuenta")]
        public String generoCuenta { get; set; }

        [JsonPropertyName("tipo_identId_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public int tipoIdentIdCuenta { get; set; }

        [JsonPropertyName("num_ident_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String numIdentCuenta { get; set; }

        [JsonPropertyName("fecha_nac_cuenta")]
        public String fechaNacCuenta { get; set; }

        [JsonPropertyName("entidad_nacId_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public int entidadNacIdCuenta { get; set; }

        [JsonPropertyName("pais_nacId_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public int paisNacIdCuenta { get; set; }

        [JsonPropertyName("nacionalidadId_cuenta")]
        public int nacionalidadIdCuenta { get; set; }

        [JsonPropertyName("serie_firma_elect_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String serieFirmaElectCuenta { get; set; }

        [JsonPropertyName("ocupacionId_cuenta")]
        public int ocupacionIdCuenta { get; set; }

        [JsonPropertyName("telefono_cuenta")]
        public String telefonoCuenta { get; set; }

        [JsonPropertyName("colonia_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String colonia_cuenta { get; set; }

        [JsonPropertyName("pais_nac_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String pais_nac_cuenta { get; set; }

        [JsonPropertyName("nacionalidad_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String nacionalidad_cuenta { get; set; }

        [JsonPropertyName("ocupacion_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String ocupacion_cuenta { get; set; }

        [JsonPropertyName("cp_cuenta")]
        [CuentaValidation(obligatorioRl: true)]
        public String cp_cuenta { get; set; }
    }
}
