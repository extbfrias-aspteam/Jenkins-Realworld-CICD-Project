using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    [DbTable("cuenta")]
	public class Cuenta
	{
		[DbColumn("clabe")]
		public string Clabe { get; set; }
        [DbColumn("udn")]
        public int Udn { get; set; }
        [DbColumn("estado")]
        public string Estado { get; set; }
        [DbColumn("token")]
        public string? Token { get; set; }
        [DbColumn("activo")]
        public bool Activo { get; set; }
        [DbColumn("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }
        [DbColumn("usuario_creacion")]
        public string? UsuarioCreacion { get; set; }
        [DbColumn("id_persona")]
        public int? IdPersona { get; set; }
        [DbColumn("id_estatus_ahorro")]
        public int? IdEstatusAhorro { get; set; }
        [DbColumn("asp_cuenta")]
        public string? AspCuenta { get; set; }
        [DbColumn("asp_id_cuenta")]
        public int? AspIdCuenta { get; set; }
        [DbColumn("uuid")]
        public string? Uuid { get; set; }
        [DbColumn("blu_black_list")]
        public bool? BluBlackList { get; set; }
        [DbColumn("blu_limite")]
        public decimal? BluMontoLimite { get; set; }
        [DbColumn("monto_permitido")]
        public bool? MontoPermitido { get; set; }
        [DbColumn("pblu")]
        public int? Pblu { get; set; }
        [DbColumn("nivel")]
        public int? Nivel { get; set; }
        [DbColumn("fecha_actualizacion")]
        public DateTime? FehaActualizacion { get; set; }
        [DbColumn("usuario_actualizacion")]
        public string? UsuarioActualizacion { get; set; }
        [DbColumn("info_adicional")]
        public string? InfoAdicional { get; set; }
        [DbColumn("no_notificar_abono")]
        public bool? NoNotificarAbono { get; set; }
        [DbColumn("actualizar")]
        public int? Actualizar { get; set; }
        [DbColumn("bloqueo")]
        public string? Bloqueo { get; set; }

    }
}
