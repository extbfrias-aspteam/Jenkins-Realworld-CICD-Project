using Asp.Api.Azul.Helpers;
namespace Asp.Api.Azul.Entities.DataBase
{
    [DbTable("persona")]
    public class Persona
    {
        [DbColumn("id_persona", pk:true)]
        public int idPersona { get; set; }

        [DbColumn("tipo_persona")]
        public string? tipoPersona { get; set; }

        [DbColumn("nombre")]
        public string? nombre { get; set; }

        [DbColumn("apellido_paterno")]
        public string? apellidoPaterno { get; set; }

        [DbColumn("apellido_materno")]
        public string? apellidoMaterno { get; set; }


        [DbColumn("razon_social")]
        public string? razonSocial { get; set; }


        [DbColumn("rfc")]
        public string? rfc { get; set; }


        [DbColumn("curp")]
        public string? curp { get; set; }


        [DbColumn("lugar_nacimiento")]
        public string? lugarNacimiento { get; set; }


        [DbColumn("id_nacionalidad")]
        public int? idNacionalidad { get; set; }

        [DbColumn("id_estado_civil")]
        public int? idEstadoCivil { get; set; }


        [DbColumn("telefono")]
        public string? telefono { get; set; }


        [DbColumn("celular")]
        public string? celular { get; set; }


        [DbColumn("correo")]
        public string? correo { get; set; }

        [DbColumn("id_ocupacion")]
        public int? idOcupacion { get; set; }


        [DbColumn("fecha_nacimiento")]
        public string? fechaNacimiento { get; set; }


        [DbColumn("sexo")]
        public string? sexo { get; set; }


        [DbColumn("calle")]
        public string? calle { get; set; }

        [DbColumn("numero_exterior")]
        public int? numeroExterior { get; set; }

        [DbColumn("numero_interior")]
        public int? numeroInterior { get; set; }

        [DbColumn("id_colonia")]
        public int? idColonia { get; set; }

        [DbColumn("id_codigo_postal")]
        public string? idCodigoPostal { get; set; }
        [DbColumn("referencia_direccion")]
        public string? referenciaDireccion { get; set; }

        [DbColumn("ingreso_mensual")]
        public decimal? ingresoMensual { get; set; }

        [DbColumn("monto_maximo_ahorro")]
        public decimal? montoMaximoAhorro { get; set; }
        [DbColumn("id_puesto")]
        public int? idPuesto { get; set; }
        [DbColumn("id_giro")]
        public int? idGiro { get; set; }
        [DbColumn("id_destino_fondo")]
        public int? idDestinoFondo { get; set; }
        [DbColumn("id_localidad")]
        public int? id_localidad { get; set; }
        [DbColumn("fecha_creacion")]
        public DateTime? fechaCreacion { get; set; }
        [DbColumn("asp_codigo_postal")]
        public string? aspCodigoPostal { get; set; }
        [DbColumn("expediente_enviado")]
        public bool? expedienteEnviado { get; set; }
        [DbColumn("colonia")]
        public string? colonia { get; set; }
        [DbColumn("serie_fiel")]
        public string? serieFiel { get; set; }

        [DbColumn("geolocalizacion")]
        public string? geolocalizacion { get; set; }

        [DbColumn("num_ext")]
        public string? numExt { get; set; }

        [DbColumn("num_int")]
        public string? numInt { get; set; }

        [DbColumn("calle2")]
        public string? calle2 { get; set; }

        [DbColumn("calle3")]
        public string? calle3 { get; set; }

        [DbColumn("id_pais")]
        public int? idPais { get; set; }

        [DbColumn("id_entidad")]
        public int? idEntidad { get; set; }

        [DbColumn("id_sociedad")]
        public int? idSociedad { get; set; }

        [DbColumn("cant_op_mensual")]
        public decimal? cantOpMensual { get; set; }

        [DbColumn("ciudad")]
        public string? ciudad { get; set; }

        [DbColumn("num_ident")]
        public string? numIdent { get; set; }

        [DbColumn("tipo_ident_id")]
        public int? tipoIdentId { get; set; }

        //[DbColumn("activo")]
        //public bool activo { get; set; }
        [DbColumn("id_grado_estudios")]
        public int? idGradoEstudios { get; set; }
        //[DbColumn("fecha_actualizacion")]
        //public DateTime fechaActualizacion { get; set; }
    }
}
