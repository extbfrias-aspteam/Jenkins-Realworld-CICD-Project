using System.Text.Json.Serialization;
using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoAspSolicitante
    {
        [JsonPropertyName("nombre_cuenta")]
        [CuentaValidation(obligatorioN1:true, obligatorioPf:true,obligatorioRl:true)]
        public string nombreCuenta { get; set; }

        [JsonPropertyName("tipo_persona_cuenta")]
        [CuentaValidation(obligatorioN1: true, obligatorioPf: true, obligatorioRl: true)]
        public string tipoPersonaCuenta { get; set; }

        [JsonPropertyName("pr_apellido_cuenta")]
        [CuentaValidation(obligatorioN1: true, obligatorioPf: true, obligatorioRl: true)]
        public string prApellidoCuenta { get; set; }

        [JsonPropertyName("sg_apellido_cuenta")]
        [CuentaValidation(obligatorioN1: true, obligatorioPf: true, obligatorioRl: true)]
        public string sgApellidoCuenta { get; set; }

        [JsonPropertyName("denominacion_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioPm:true)]
        public int denominacionCuenta { get; set; }

        [JsonPropertyName("rfc_cuenta")]
        [CuentaValidation(obligatorioN1:true, obligatorioA:true,obligatorioRl:true)]
        [DataValidation(rfcValidation: true)]
        public string rfcCuenta { get; set; }

        [JsonPropertyName("curp_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioPf:true,obligatorioRl:true)]
        [DataValidation(curpValidation: true)]
        public string curpCuenta { get; set; }

        [JsonPropertyName("calle_principal_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioA:true,obligatorioN2:true,obligatorioRl:true)]
        public string callePrincipalCuenta { get; set; }

        [JsonPropertyName("calle_secundaria_cuenta")]
        public string calleSecundariaCuenta { get; set; }

        [JsonPropertyName("calle_secundaria2_cuenta")]
        public string calleSecundaria2Cuenta { get; set; }

        [JsonPropertyName("no_interior_cuenta")]
        public string noInteriorCuenta { get; set; }

        [JsonPropertyName("no_exterior_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioA:true,obligatorioRl:true)]
        public string noExteriorCuenta { get; set; }
        
        [JsonPropertyName("coloniaId_cuenta")]
        public string coloniaIdCuenta { get; set; }

        [JsonPropertyName("cp_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioA:true,obligatorioN2:true,obligatorioRl:true)]
        public string cp_cuenta { get; set; }

        [JsonPropertyName("ciudad_cuenta")]
        public string ciudadCuenta { get; set; }

        [JsonPropertyName("celular_cuenta")]
        [CuentaValidation(obligatorioN3yN4:true, obligatorioA:true)]
        public string celularCuenta { get; set; }

        [JsonPropertyName("correo_cuenta")]
        [CuentaValidation(obligatorioN3yN4:true,obligatorioA:true)]
        public string correoCuenta { get; set; }

        [JsonPropertyName("genero_cuenta")]
        [CuentaValidation(obligatorioN3yN4:true,obligatorioPf:true)]
        public string generoCuenta { get; set; }

        [JsonPropertyName("tipo_identId_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioRl:true,obligatorioN2:true,obligatorioPf:true)]
        public int tipoIdentIdCuenta { get; set; }

        [JsonPropertyName("num_ident_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioRl:true,obligatorioN2:true,obligatorioPf:true)]

        public string numIdentCuenta { get; set; }

        [JsonPropertyName("fecha_nac_cuenta")]
        [CuentaValidation(obligatorioN2:true,obligatorioA:true)]
        public string fechaNacCuenta { get; set; }

        [JsonPropertyName("entidad_nacId_cuenta")]
        [CuentaValidation(obligatorioN2:true,obligatorioA:true,obligatorioRl:true)]
        public int entidadNacIdCuenta { get; set; }


        [JsonPropertyName("pais_nacId_cuenta")]
        
        public int paisNacIdCuenta { get; set; }


        [JsonPropertyName("nacionalidadId_cuenta")]
        public int nacionalidadIdCuenta { get; set; }


        [JsonPropertyName("serie_firma_elect_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioRl:true,obligatorioA:true)]
        [CuentaValidation(obligatorioN2: true, obligatorioRl: true, obligatorioN3yN4: true, obligatorioA: true)]
        public string serieFirmaElectCuenta { get; set; }


        [JsonPropertyName("ocupacionId_cuenta")]
        public int ocupacionIdCuenta { get; set; }


        [JsonPropertyName("telefono_cuenta")]
        [CuentaValidation(obligatorioN3yN4:true,obligatorioA:true)]
        public string telefonoCuenta { get; set; }

        [JsonPropertyName("geolocalizacion_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioA:true)]
        [DataValidation(geolocalizacionValidation: true)]
        public string geolocalizacionCuenta { get; set; }

        [JsonPropertyName("unidad_negocio_cuenta")]
        [CuentaValidation(obligatorioN1:true)]
        public int unidadNegocioCuenta { get; set; }

        [JsonPropertyName("nivel_cuenta")]
        [CuentaValidation(obligatorioN1:true)]
        public string nivelCuenta { get; set; }

        [JsonPropertyName("monto_max_aho_cuenta")]
        public string montoMaxAhoCuenta { get; set; }

        [JsonPropertyName("ingresos_cuenta")]
        public string ingresosCuenta { get; set; }

        [JsonPropertyName("colonia_cuenta")]
        [CuentaValidation(obligatorioN1:true,obligatorioA:true,obligatorioN2:true,obligatorioRl:true)]
        public string colonia_cuenta { get; set; }

        [JsonPropertyName("pais_nac_cuenta")]
        [CuentaValidation(obligatorioN2: true, obligatorioRl: true)]
        public string pais_nac_cuenta { get; set; }

        [JsonPropertyName("nacionalidad_cuenta")]
        [CuentaValidation(obligatorioN2: true, obligatorioA: true, obligatorioRl: true)]
        public string nacionalidad_cuenta { get; set; }

        [JsonPropertyName("ocupacion_cuenta")]
        [CuentaValidation(obligatorioN2: true, obligatorioRl: true, obligatorioN3yN4: true, obligatorioA: true)]
        public string ocupacion_cuenta { get; set; }
    }
}
